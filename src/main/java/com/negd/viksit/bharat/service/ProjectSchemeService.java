package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.ProjectSchemeDto;
import com.negd.viksit.bharat.dto.ProjectSchemeResponseDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ProjectScheme;
import com.negd.viksit.bharat.model.SchemeKeyDeliverable;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.ProjectSchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectSchemeService {

    private final ProjectSchemeRepository projectRepo;
    private final DocumentRepository documentRepo;
    private final MinistryRepository ministryRepository;
    private final ResBuildder responseBuilder;


    public ProjectSchemeResponseDto create(ProjectSchemeDto dto,User user) {
        Ministry ministry = ministryRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));
        ProjectScheme project = ProjectScheme.builder()
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .ministry(ministry)
                .description(dto.getDescription())
                .targetDate(dto.getTargetDate())
                .totalBudgetRequired(dto.getTotalBudgetRequired())
                .beneficiariesNo(dto.getBeneficiariesNo())
                .build();

        if (dto.getKeyDeliverables() != null) {
            List<SchemeKeyDeliverable> deliverables = dto.getKeyDeliverables().stream().map(d -> {
                Document doc = d.getDocumentId() != null
                        ? documentRepo.findById(d.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;
                return SchemeKeyDeliverable.builder()
                        .activityDescription(d.getActivityDescription())
                        .deadline(d.getDeadline())
                        .progressMade(d.getProgressMade())
                        .document(doc)
                        .projectScheme(project)
                        .build();
            }).toList();
            project.setKeyDeliverables(deliverables);
        }

        ProjectScheme saved = projectRepo.save(project);
        return responseBuilder.mapToResponse(saved,user);
    }

    public ProjectSchemeResponseDto getById(String id,User user) {
        return responseBuilder.mapToResponse(projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found")),user);
    }

    public List<ProjectSchemeResponseDto> getAll(User user) {
        return projectRepo.findAll().stream().map(goal -> responseBuilder.mapToResponse(goal,user) ).toList();
    }

    public ProjectSchemeResponseDto update(String id, ProjectSchemeDto dto,User user) {
        Ministry ministry = ministryRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));
        ProjectScheme project = projectRepo.findByEntityId(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(dto.getName());
        project.setType(dto.getType());
        project.setMinistry(ministry);
        project.setStatus(dto.getStatus());
        project.setDescription(dto.getDescription());
        project.setTargetDate(dto.getTargetDate());
        project.setTotalBudgetRequired(dto.getTotalBudgetRequired());
        project.setBeneficiariesNo(dto.getBeneficiariesNo());

        project.getKeyDeliverables().clear();
        if (dto.getKeyDeliverables() != null) {
            List<SchemeKeyDeliverable> deliverables = dto.getKeyDeliverables().stream().map(d -> {
                Document doc = d.getDocumentId() != null
                        ? documentRepo.findById(d.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;
                return SchemeKeyDeliverable.builder()
                        .activityDescription(d.getActivityDescription())
                        .deadline(d.getDeadline())
                        .progressMade(d.getProgressMade())
                        .document(doc)
                        .projectScheme(project)
                        .build();
            }).toList();
            project.getKeyDeliverables().addAll(deliverables);
        }

        return responseBuilder.mapToResponse(projectRepo.save(project),user);
    }

    public void delete(String id) {
        ProjectScheme project = projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        projectRepo.delete(project);
    }


    public List<ProjectSchemeResponseDto> filterProjects(User usr, String st) {
        final var id = usr.getEntityid();
        final var scopedRoles = Set.of(Constants.MADMIN,Constants.DADMIN);
        final boolean isScoped = scopedRoles.stream().anyMatch(usr::hasRole);

        Supplier<List<ProjectScheme>> fetchProjects = () -> {
            if (isScoped) {
                return st != null
                        ? projectRepo.findByCreatedByAndStatusIgnoreCase(id, st)
                        : projectRepo.findByCreatedBy(id);
            }
            return st != null
                    ? projectRepo.findByStatusIgnoreCase(st)
                    : projectRepo.findAll();
        };

        return Optional.ofNullable(st)
                .map(s -> fetchProjects.get())
                .orElseGet(fetchProjects)
                .stream()
                .map(projectScheme -> responseBuilder.mapToResponse(projectScheme,usr))
                .filter(Objects::nonNull)
                .toList();
    }

}

