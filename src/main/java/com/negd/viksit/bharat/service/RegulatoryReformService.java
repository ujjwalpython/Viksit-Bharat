package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ReformMilestone;
import com.negd.viksit.bharat.model.RegulatoryReform;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.RegulatoryReformRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegulatoryReformService {

    private final RegulatoryReformRepository reformRepository;
    private final DocumentRepository documentRepository;
    private final MinistryRepository ministryRepository;

    public RegulatoryRespReformDto createReform(RegulatoryReformDto dto) {
        Ministry ministry = ministryRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));

        RegulatoryReform reform = RegulatoryReform.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .reformType(dto.getReformType())
                .ministry(ministry)
                .targetCompletionDate(dto.getTargetCompletionDate())
                .rulesToBeAmended(dto.getRulesToBeAmended())
                .intendedOutcome(dto.getIntendedOutcome())
                .presentStatus(dto.getPresentStatus())
                .status(dto.getStatus())
                .build();

        if (dto.getMilestones() != null) {
            List<ReformMilestone> milestones = dto.getMilestones().stream().map(m -> {
                Document doc = m.getDocumentId() != null
                        ? documentRepository.findById(m.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;
                return ReformMilestone.builder()
                        .activityDescription(m.getActivityDescription())
                        .deadline(m.getDeadline())
                        .document(doc)
                        .sortOrder(m.getSortOrder())
                        .reform(reform)
                        .build();
            }).toList();

            reform.setMilestones(milestones);
        }

        RegulatoryReform saved = reformRepository.save(reform);
        return mapToResponse(saved);
    }

    public RegulatoryRespReformDto getReformById(String id) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        return mapToResponse(reform);
    }

    public List<RegulatoryRespReformDto> getAllReforms() {
        return reformRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RegulatoryRespReformDto updateReform(String id, RegulatoryReformDto dto) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        Ministry ministry = ministryRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));

        reform.setName(dto.getName());
        reform.setDescription(dto.getDescription());
        reform.setReformType(dto.getReformType());
        reform.setMinistry(ministry);
        reform.setTargetCompletionDate(dto.getTargetCompletionDate());
        reform.setRulesToBeAmended(dto.getRulesToBeAmended());
        reform.setIntendedOutcome(dto.getIntendedOutcome());
        reform.setPresentStatus(dto.getPresentStatus());
        reform.setStatus(dto.getStatus());

        reform.getMilestones().clear();
        if (dto.getMilestones() != null) {
            List<ReformMilestone> milestones = dto.getMilestones().stream().map(m -> {
                Document doc = m.getDocumentId() != null
                        ? documentRepository.findById(m.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;
                return ReformMilestone.builder()
                        .activityDescription(m.getActivityDescription())
                        .deadline(m.getDeadline())
                        .document(doc)
                        .sortOrder(m.getSortOrder())
                        .reform(reform)
                        .build();
            }).toList();
            reform.getMilestones().addAll(milestones);
        }

        RegulatoryReform updated = reformRepository.save(reform);
        return mapToResponse(updated);
    }

    public void deleteReform(String id) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        reformRepository.delete(reform);
    }

    private RegulatoryRespReformDto mapToResponse(RegulatoryReform reform) {
        return RegulatoryRespReformDto.builder()
                .id(reform.getEntityId())
                .name(reform.getName())
                .description(reform.getDescription())
                .LastUpdate(reform.getUpdatedOn())
                .reformType(reform.getReformType())
                .ministry(reform.getMinistry() != null ? reform.getMinistry().getName() : null) // ✅ null-safe
                .targetCompletionDate(reform.getTargetCompletionDate())
                .rulesToBeAmended(reform.getRulesToBeAmended())
                .intendedOutcome(reform.getIntendedOutcome())
                .presentStatus(reform.getPresentStatus())
                .status(reform.getStatus())
                .milestones(reform.getMilestones() != null
                        ? reform.getMilestones().stream()
                        .map(m -> ReformMilestoneRespDto.builder()
                                .id(m.getEntityId())
                                .activityDescription(m.getActivityDescription())
                                .deadline(m.getDeadline())
                                .sortOrder(m.getSortOrder())
                                .documentId(m.getDocument() != null
                                        ? m.getDocument().getId()
                                        : null
                                )
                                .build()
                        )
                        .toList()
                        : List.of()
                )
                .build();
    }

    public List<RegulatoryRespReformDto> filterReforms(Long entityId, String status) {
        List<RegulatoryReform> reforms;

        if (status == null) {
            reforms = reformRepository.findByCreatedBy(entityId);
        } else {
            reforms = reformRepository.findByCreatedByAndStatusIgnoreCase(entityId, status);
        }

        return reforms.stream().map(this::mapToResponse).toList();
    }

}
