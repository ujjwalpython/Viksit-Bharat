package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.DocumentDto;
import com.negd.viksit.bharat.dto.SchemeKeyDeliverableRespDto;
import com.negd.viksit.bharat.dto.ProjectSchemeDto;
import com.negd.viksit.bharat.dto.ProjectSchemeResponseDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ProjectScheme;
import com.negd.viksit.bharat.model.SchemeKeyDeliverable;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.ProjectSchemeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectSchemeService {

    private final ProjectSchemeRepository projectRepo;
    private final DocumentRepository documentRepo;
    private final MinistryRepository ministryRepository;

    public ProjectSchemeResponseDto create(ProjectSchemeDto dto) {
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
        return mapToResponse(saved);
    }

    public ProjectSchemeResponseDto getById(String id) {
        return mapToResponse(projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found")));
    }

    public List<ProjectSchemeResponseDto> getAll() {
        return projectRepo.findAll().stream().map(this::mapToResponse).toList();
    }

    public ProjectSchemeResponseDto update(String id, ProjectSchemeDto dto) {
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

        return mapToResponse(projectRepo.save(project));
    }

    public void delete(String id) {
        ProjectScheme project = projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        projectRepo.delete(project);
    }

    private ProjectSchemeResponseDto mapToResponse(ProjectScheme project) {
        return ProjectSchemeResponseDto.builder()
                .id(project.getEntityId())
                .name(project.getName())
                .type(project.getType())
                .description(project.getDescription())
                .targetDate(project.getTargetDate())
                .totalBudgetRequired(project.getTotalBudgetRequired())
                .beneficiariesNo(project.getBeneficiariesNo())
                .status(project.getStatus())
                .ministry(project.getMinistry().getName())
                .LastUpdate(project.getUpdatedOn())
                .keyDeliverables(project.getKeyDeliverables() != null
                                ? project.getKeyDeliverables().stream().map(k ->
                                SchemeKeyDeliverableRespDto.builder()
                                        .id(k.getEntityId())
                                        .activityDescription(k.getActivityDescription())
                                        .deadline(k.getDeadline())
                                        .progressMade(k.getProgressMade())
                                        .documentId(k.getDocument() != null
                                                ? k.getDocument().getId()
                                                : null
                                        )
                                        .build()
                                )
                                .toList()
                                : List.of()
                )
                .build();
    }

    public List<ProjectSchemeResponseDto> filterProjects(Long entityId, String status) {
        List<ProjectScheme> projects;

        if (status == null) {
            projects = projectRepo.findByCreatedBy(entityId);
        } else {
            projects = projectRepo.findByCreatedByAndStatusIgnoreCase(entityId, status);
        }

        return projects.stream()
                .map(this::mapToResponse)
                .toList();
    }

}

