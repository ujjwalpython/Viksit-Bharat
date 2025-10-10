package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.ProjectSchemeDto;
import com.negd.viksit.bharat.dto.ProjectSchemeResponseDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ProjectScheme;
import com.negd.viksit.bharat.model.SchemeKeyDeliverable;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectSchemeService {

    private final ProjectSchemeRepository projectRepo;
    private final DocumentRepository documentRepo;
    private final ResBuildder responseBuilder;
    private final GovernmentEntityRepository governmentEntityRepository;
    private final SchemeDeliverableRepository schemeKeyDeliverableRepository;


    @Transactional
    public ProjectSchemeResponseDto create(ProjectSchemeDto dto) {

        GovernmentEntity entity = governmentEntityRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        // Step 1: Create and save the parent first
        ProjectScheme project = ProjectScheme.builder()
                .name(dto.getName())
                .type(dto.getType())
                .status(dto.getStatus())
                .governmentEntity(entity)
                .description(dto.getDescription())
                .targetDate(dto.getTargetDate())
                .totalBudgetRequired(dto.getTotalBudgetRequired())
                .beneficiariesNo(dto.getBeneficiariesNo())
                .keyDeliverables(new ArrayList<>()) // initialize to avoid NPE
                .build();

        // Persist to generate seqNum and entityId
        ProjectScheme saved = projectRepo.saveAndFlush(project);

        // Step 2: Attach key deliverables (if present)
        if (dto.getKeyDeliverables() != null && !dto.getKeyDeliverables().isEmpty()) {
            AtomicInteger counter = new AtomicInteger(1);

            dto.getKeyDeliverables().forEach(d -> {
                Document doc = (d.getDocumentId() != null)
                        ? documentRepo.findById(d.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;

                String suffix = String.format("%02d", counter.getAndIncrement());
                String deliverableId = saved.getEntityId() + "/" + suffix;

                SchemeKeyDeliverable deliverable = SchemeKeyDeliverable.builder()
                        .entityId(deliverableId)
                        .activityDescription(d.getActivityDescription())
                        .deadline(d.getDeadline())
                        .progressMade(d.getProgressMade())
                        .document(doc)
                        .projectScheme(saved)
                        .build();

                saved.getKeyDeliverables().add(deliverable);
            });

            // No need to call schemeKeyDeliverableRepository.saveAll()
            // because cascade = ALL will persist children automatically
            projectRepo.save(saved);
        }

        return responseBuilder.mapToResponse(saved);
    }

    public ProjectSchemeResponseDto getById(String id) {
        return responseBuilder.mapToResponse(projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found")));
    }

    public List<ProjectSchemeResponseDto> getAll(User user) {
        return projectRepo.findAll().stream().map(goal -> responseBuilder.mapToResponse(goal) ).toList();
    }

    @Transactional
    public ProjectSchemeResponseDto update(String id, ProjectSchemeDto dto, User user) {

        GovernmentEntity governmentEntity = governmentEntityRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        ProjectScheme project = projectRepo.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Update project fields
        project.setName(dto.getName());
        project.setType(dto.getType());
        project.setGovernmentEntity(governmentEntity);
        project.setStatus(dto.getStatus());
        project.setDescription(dto.getDescription());
        project.setTargetDate(dto.getTargetDate());
        project.setTotalBudgetRequired(dto.getTotalBudgetRequired());
        project.setBeneficiariesNo(dto.getBeneficiariesNo());

        // Clear old deliverables (orphanRemoval = true ensures DB deletes)
        project.getKeyDeliverables().clear();

        // Add new deliverables if present
        if (dto.getKeyDeliverables() != null && !dto.getKeyDeliverables().isEmpty()) {
            AtomicInteger counter = new AtomicInteger(1);

            dto.getKeyDeliverables().forEach(d -> {
                Document doc = (d.getDocumentId() != null)
                        ? documentRepo.findById(d.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;

                String suffix = String.format("%02d", counter.getAndIncrement());
                String deliverableId = project.getEntityId() + "/" + suffix;

                SchemeKeyDeliverable deliverable = SchemeKeyDeliverable.builder()
                        .entityId(deliverableId)
                        .activityDescription(d.getActivityDescription())
                        .deadline(d.getDeadline())
                        .progressMade(d.getProgressMade())
                        .document(doc)
                        .projectScheme(project)
                        .build();

                project.getKeyDeliverables().add(deliverable);
            });
        }

        // Save project — cascade persists deliverables automatically
        ProjectScheme updated = projectRepo.save(project);

        return responseBuilder.mapToResponse(updated);
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
                .map(projectScheme -> responseBuilder.mapToResponse(projectScheme))
                .filter(Objects::nonNull)
                .toList();
    }

}

