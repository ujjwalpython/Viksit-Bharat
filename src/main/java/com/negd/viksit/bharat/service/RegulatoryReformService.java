package com.negd.viksit.bharat.service;

import org.springframework.stereotype.Service;

//
//import java.util.List;
//
//import org.hibernate.validator.constraints.UUID;
//import org.springframework.stereotype.Service;
//
//import com.negd.viksit.bharat.dto.MilestoneDto;
//import com.negd.viksit.bharat.exception.InvalidStatusException;
//import com.negd.viksit.bharat.model.Milestone;
//import com.negd.viksit.bharat.model.RegulatoryReform;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//
@Service
//@Transactional
//@RequiredArgsConstructor
public class RegulatoryReformService {
//
//    private final RegulatoryReformRepository reformRepository;
//
//    public RegulatoryReformDto createReform(RegulatoryReformDto dto) {
//        RegulatoryReform entity = mapToEntity(dto);
//        entity.getMilestones().forEach(m -> m.setReform(entity));
//        RegulatoryReform saved = reformRepository.save(entity);
//        return mapToDto(saved);
//    }
//
//    public List<RegulatoryReformDto> getAllReforms() {
//        return reformRepository.findAll()
//                .stream()
//                .map(this::mapToDto)
//                .toList();
//    }
//
//    public RegulatoryReformDto getReform(UUID id) {
//        return reformRepository.findById(id)
//                .map(this::mapToDto)
//                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
//    }
//
//    public RegulatoryReformDto updateReform(UUID id, RegulatoryReformDto dto) {
//        RegulatoryReform existing = reformRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
//
//        existing.setName(dto.getName());
//        existing.setDescription(dto.getDescription());
//        existing.setReformType(dto.getReformType());
//        existing.setTargetCompletionDate(dto.getTargetCompletionDate());
//        existing.setRulesToBeAmended(dto.getRulesToBeAmended());
//        existing.setIntendedOutcome(dto.getIntendedOutcome());
//        existing.setPresentStatus(dto.getPresentStatus());
//        existing.setStatus(dto.getStatus());
//
//        existing.getMilestones().clear();
//        if (dto.getMilestones() != null) {
//            List<Milestone> milestones = dto.getMilestones().stream()
//                    .map(mDto -> mapMilestoneToEntity(mDto, existing))
//                    .toList();
//            existing.getMilestones().addAll(milestones);
//        }
//
//        return mapToDto(reformRepository.save(existing));
//    }
//
//    public void deleteReform(UUID id) {
//        RegulatoryReform entity = reformRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
//        reformRepository.delete(entity);
//    }
//
//    public List<RegulatoryReformDto> filterReforms(Long entityId, String status) {
//        List<RegulatoryReform> reforms;
//
//        if (status == null) {
//            reforms = reformRepository.findByCreatedBy(entityId);
//        } else {
//            reforms = reformRepository.findByCreatedByAndStatusIgnoreCase(entityId, status);
//        }
//
//        return reforms.stream()
//                .map(this::mapToDto)
//                .toList();
//    }
//
//    public RegulatoryReformDto updateStatus(UUID id, String status) {
//        RegulatoryReform reform = reformRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Reform not found with id: " + id));
//
//        String newStatus = status.toUpperCase();
//        switch (newStatus) {
//            case "DRAFT":
//            case "SUBMITTED":
//                reform.setStatus(newStatus);
//                break;
//            default:
//                throw new InvalidStatusException("Invalid status: " + newStatus);
//        }
//
//        RegulatoryReform saved = reformRepository.save(reform);
//        return mapToDto(saved);
//    }
//
//    // ---- Mapping Helpers ----
//    private RegulatoryReform mapToEntity(RegulatoryReformDto dto) {
//        return RegulatoryReform.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .description(dto.getDescription())
//                .reformType(dto.getReformType())
//                .targetCompletionDate(dto.getTargetCompletionDate())
//                .rulesToBeAmended(dto.getRulesToBeAmended())
//                .intendedOutcome(dto.getIntendedOutcome())
//                .presentStatus(dto.getPresentStatus())
//                .status(dto.getStatus())
//                .milestones(dto.getMilestones() != null ?
//                        dto.getMilestones().stream()
//                                .map(m -> mapMilestoneToEntity(m, null))
//                                .toList() : List.of())
//                .build();
//    }
//
//    private RegulatoryReformDto mapToDto(RegulatoryReform entity) {
//        return RegulatoryReformDto.builder()
//                .id(entity.getId())
//                .name(entity.getName())
//                .description(entity.getDescription())
//                .reformType(entity.getReformType())
//                .targetCompletionDate(entity.getTargetCompletionDate())
//                .rulesToBeAmended(entity.getRulesToBeAmended())
//                .intendedOutcome(entity.getIntendedOutcome())
//                .presentStatus(entity.getPresentStatus())
//                .status(entity.getStatus())
//                .milestones(entity.getMilestones() != null ?
//                        entity.getMilestones().stream()
//                                .map(this::mapMilestoneToDto)
//                                .toList() : List.of())
//                .build();
//    }
//
//    private Milestone mapMilestoneToEntity(MilestoneDto dto, RegulatoryReform parent) {
//        return Milestone.builder()
//                .id(dto.getId())
//                .activityDescription(dto.getActivityDescription())
//                .deadline(dto.getDeadline())
//                .documentPath(dto.getDocumentPath())
//                .reform(parent)
//                .build();
//    }
//
//    private MilestoneDto mapMilestoneToDto(Milestone entity) {
//        return MilestoneDto.builder()
//                .id(entity.getId())
//                .activityDescription(entity.getActivityDescription())
//                .deadline(entity.getDeadline())
//                .documentPath(entity.getDocumentPath())
//                .build();
//    }
}
//
