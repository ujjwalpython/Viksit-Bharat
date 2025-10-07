package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ReformMilestone;
import com.negd.viksit.bharat.model.RegulatoryReform;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.RegulatoryReformRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class RegulatoryReformService {

    private final RegulatoryReformRepository reformRepository;
    private final DocumentRepository documentRepository;
    private final MinistryRepository ministryRepository;
    private final ResBuildder responseBuilder;


    public RegulatoryRespReformDto createReform(RegulatoryReformDto dto,User user) {
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
        return responseBuilder.mapToResponse(saved,user);
    }

    public RegulatoryRespReformDto getReformById(String id,User user) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        return responseBuilder.mapToResponse(reform,user);
    }

    public List<RegulatoryRespReformDto> getAllReforms(User user) {
        return reformRepository.findAll().stream()
                .map(reform -> responseBuilder.mapToResponse(reform,user))
                .toList();
    }

    public RegulatoryRespReformDto updateReform(String id, RegulatoryReformDto dto,User user) {
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
        return responseBuilder.mapToResponse(updated,user);
    }

    public void deleteReform(String id) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        reformRepository.delete(reform);
    }

    @Transactional(readOnly = true)
    public List<RegulatoryRespReformDto> filterReforms(User usr, String st) {
        final var id = usr.getEntityid();
        final var admin = Set.of(Constants.MADMIN,Constants.DADMIN).stream().anyMatch(usr::hasRole);

        Function<Boolean, List<RegulatoryReform>> fetch = x -> {
            if (admin)
                return Boolean.TRUE.equals(x)
                        ? reformRepository.findByCreatedByAndStatusIgnoreCase(id, st)
                        : reformRepository.findByCreatedBy(id);
            return Boolean.TRUE.equals(x)
                    ? reformRepository.findByStatusIgnoreCase(st)
                    : reformRepository.findAll();
        };

        return Stream.ofNullable(st)
                .map(Objects::nonNull)
                .map(fetch)
                .findFirst()
                .orElseGet(() -> fetch.apply(false))
                .stream()
                .filter(Objects::nonNull)
                .map(reform -> responseBuilder.mapToResponse(reform, usr))
                .toList();
    }

}
