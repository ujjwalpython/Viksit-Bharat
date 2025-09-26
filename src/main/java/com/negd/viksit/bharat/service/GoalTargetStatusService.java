package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.DocumentDto;
import com.negd.viksit.bharat.dto.GoalTargetStatusDto;
import com.negd.viksit.bharat.dto.MilestoneDto;
import com.negd.viksit.bharat.exception.InvalidStatusException;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.GoalTargetStatus;
import com.negd.viksit.bharat.model.Milestone;
import com.negd.viksit.bharat.repository.GoalTargetStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalTargetStatusService {

    private final GoalTargetStatusRepository goalTargetStatusRepository;

    public GoalTargetStatusService(GoalTargetStatusRepository goalTargetStatusRepository) {
        this.goalTargetStatusRepository = goalTargetStatusRepository;
    }

    public GoalTargetStatusDto createGoalTargetStatus(GoalTargetStatusDto dto) {
        GoalTargetStatus entity = mapToEntity(dto);
        GoalTargetStatus saved = goalTargetStatusRepository.save(entity);
        return mapToDto(saved);
    }

    public List<GoalTargetStatusDto> getAllTargetStatuses() {
        return goalTargetStatusRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public GoalTargetStatusDto getGoalTargetStatus(UUID id) {
        return goalTargetStatusRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("TargetStatus not found"));
    }

    public GoalTargetStatusDto updateGoalTargetStatus(UUID id, GoalTargetStatusDto dto) {
        GoalTargetStatus existing = goalTargetStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TargetStatus not found"));

        existing.setTarget(dto.getTarget());
        existing.setActionPoint(dto.getActionPoint());
        existing.setPresentStatusOfAchievement(dto.getPresentStatusOfAchievement());
        existing.setBottlenecks(dto.getBottlenecks());
        existing.setPriority(dto.getPriority());
        existing.setCompletionDate(dto.getCompletionDate());
        existing.setStatus(dto.getStatus());
        existing.getMilestones().clear();
        if (dto.getMilestones() != null) {
            List<Milestone> milestones = dto.getMilestones().stream()
                    .map(mDto -> mapMilestoneToEntity(mDto, existing))
                    .collect(Collectors.toList());
            existing.getMilestones().addAll(milestones);
        }

        GoalTargetStatus updated = goalTargetStatusRepository.save(existing);
        return mapToDto(updated);
    }

    public void deleteGoalTargetStatus(UUID id) {
        GoalTargetStatus targetStatus = goalTargetStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TargetStatus not found"));
        goalTargetStatusRepository.delete(targetStatus);
    }


    private GoalTargetStatus mapToEntity(GoalTargetStatusDto dto) {
        GoalTargetStatus entity = new GoalTargetStatus();
        entity.setTarget(dto.getTarget());
        entity.setActionPoint(dto.getActionPoint());
        entity.setPresentStatusOfAchievement(dto.getPresentStatusOfAchievement());
        entity.setBottlenecks(dto.getBottlenecks());
        entity.setPriority(dto.getPriority());
        entity.setCompletionDate(dto.getCompletionDate());
        entity.setStatus(dto.getStatus());
        if (dto.getMilestones() != null) {
            List<Milestone> milestones = dto.getMilestones().stream()
                    .map(mDto -> mapMilestoneToEntity(mDto, entity))
                    .collect(Collectors.toList());
            entity.setMilestones(milestones);
        }
        return entity;
    }

    private GoalTargetStatusDto mapToDto(GoalTargetStatus entity) {
        GoalTargetStatusDto dto = new GoalTargetStatusDto();
        dto.setId(entity.getId());
        dto.setTarget(entity.getTarget());
        dto.setActionPoint(entity.getActionPoint());
        dto.setPresentStatusOfAchievement(entity.getPresentStatusOfAchievement());
        dto.setBottlenecks(entity.getBottlenecks());
        dto.setPriority(entity.getPriority());
        dto.setCompletionDate(entity.getCompletionDate());
        dto.setStatus(entity.getStatus());
        if (entity.getMilestones() != null) {
            List<MilestoneDto> milestoneDtos = entity.getMilestones().stream()
                    .map(this::mapMilestoneToDto)
                    .collect(Collectors.toList());
            dto.setMilestones(milestoneDtos);
        }
        return dto;
    }

    private Milestone mapMilestoneToEntity(MilestoneDto dto, GoalTargetStatus parent) {
        Milestone milestone = new Milestone();
        milestone.setActivityDescription(dto.getActivityDescription());
        milestone.setDeadline(dto.getDeadline());
        milestone.setGoalTargetStatus(parent);
        milestone.setSortOrder(dto.getSortOrder());
       /* if (dto.getDocument() != null) {
            milestone.setDocument(mapDocumentToEntity(dto.getDocument()));
        }*/
        return milestone;
    }

    private MilestoneDto mapMilestoneToDto(Milestone entity) {
        MilestoneDto dto = new MilestoneDto();
        dto.setId(entity.getId());
        dto.setActivityDescription(entity.getActivityDescription());
        dto.setDeadline(entity.getDeadline());
        dto.setSortOrder(entity.getSortOrder());

/*
        if (entity.getDocument() != null) {
            dto.setDocument(mapDocumentToDto(entity.getDocument()));
        }
*/
        return dto;
    }

    private Document mapDocumentToEntity(DocumentDto dto) {
        Document doc = new Document();
        doc.setFileName(dto.getFileName());
        doc.setFileType(dto.getFileType());
        doc.setFileUrl(dto.getFileUrl());
        doc.setSize(dto.getSize());
        doc.setReferenceType(dto.getReferenceType());
        doc.setReferenceId(dto.getReferenceId());
        return doc;
    }

    private DocumentDto mapDocumentToDto(Document entity) {
        DocumentDto dto = new DocumentDto();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileType(entity.getFileType());
        dto.setFileUrl(entity.getFileUrl());
        dto.setSize(entity.getSize());
        dto.setReferenceType(entity.getReferenceType());
        dto.setReferenceId(entity.getReferenceId());
        return dto;
    }

    public List<GoalTargetStatusDto> filterTargetStatuses(Long entityId, String status) {
        List<GoalTargetStatus> statuses;

        if (status == null) {
            statuses = goalTargetStatusRepository.findByCreatedBy(entityId);
        } else {
            statuses = goalTargetStatusRepository.findByCreatedByAndStatusIgnoreCase(entityId, status);
        }

        return statuses.stream()
                .map(this::mapToDto)
                .toList();
    }

    public GoalTargetStatusDto updateStatus(UUID id, String status) {
        GoalTargetStatus targetStatus = goalTargetStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TargetStatus not found with id: " + id));

        String newStatus = status.toUpperCase();
        switch (newStatus) {
            case "DRAFT":
            case "SUBMITTED":
                targetStatus.setStatus(newStatus);
                break;
            default:
                throw new InvalidStatusException("Invalid status: " + newStatus);
        }

        GoalTargetStatus saved = goalTargetStatusRepository.save(targetStatus);
        return mapToDto(saved);
    }

}

