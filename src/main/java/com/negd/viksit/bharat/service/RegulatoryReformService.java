package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.ReformMilestone;
import com.negd.viksit.bharat.model.RegulatoryReform;
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
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class RegulatoryReformService {

    private final RegulatoryReformRepository reformRepository;
    private final DocumentRepository documentRepository;
    private final ResBuildder responseBuilder;
    private final GovernmentEntityRepository governmentEntityRepository;
    private final ReformMilestoneRepository reformMilestoneRepository;


    @Transactional
    public RegulatoryRespReformDto createReform(RegulatoryReformDto dto) {

        GovernmentEntity governmentEntity = governmentEntityRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        // Create the reform entity
        RegulatoryReform reform = RegulatoryReform.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .reformType(dto.getReformType())
                .governmentEntity(governmentEntity)
                .targetCompletionDate(dto.getTargetCompletionDate())
                .rulesToBeAmended(dto.getRulesToBeAmended())
                .intendedOutcome(dto.getIntendedOutcome())
                .presentStatus(dto.getPresentStatus())
                .status(dto.getStatus())
                .milestones(new ArrayList<>()) // initialize list
                .build();

        // Save reform first to generate seqNum and entityId
        RegulatoryReform savedReform = reformRepository.saveAndFlush(reform);

        // Add milestones if present
        if (dto.getMilestones() != null && !dto.getMilestones().isEmpty()) {
            AtomicInteger counter = new AtomicInteger(1);

            dto.getMilestones().forEach(m -> {
                Document doc = (m.getDocumentId() != null)
                        ? documentRepository.findById(m.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;

                String suffix = String.format("%02d", counter.getAndIncrement());
                String milestoneId = savedReform.getEntityId() + "/" + suffix;

                ReformMilestone milestone = ReformMilestone.builder()
                        .entityId(milestoneId)
                        .activityDescription(m.getActivityDescription())
                        .deadline(m.getDeadline())
                        .document(doc)
                        .sortOrder(m.getSortOrder())
                        .reform(savedReform)
                        .build();

                savedReform.getMilestones().add(milestone);
            });

            // No need to call reformMilestoneRepository.saveAll()
            // Cascade = ALL will persist milestones automatically
            reformRepository.save(savedReform);
        }

        return responseBuilder.mapToResponse(savedReform);
    }

    public RegulatoryRespReformDto getReformById(String id) {
        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));
        return responseBuilder.mapToResponse(reform);
    }

    public List<RegulatoryRespReformDto> getAllReforms() {
        return reformRepository.findAll().stream()
                .map(reform -> responseBuilder.mapToResponse(reform))
                .toList();
    }

    @Transactional
    public RegulatoryRespReformDto updateReform(String id, RegulatoryReformDto dto) {

        RegulatoryReform reform = reformRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Reform not found"));

        GovernmentEntity governmentEntity = governmentEntityRepository.findById(dto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        // Update main reform fields
        reform.setName(dto.getName());
        reform.setDescription(dto.getDescription());
        reform.setReformType(dto.getReformType());
        reform.setGovernmentEntity(governmentEntity);
        reform.setTargetCompletionDate(dto.getTargetCompletionDate());
        reform.setRulesToBeAmended(dto.getRulesToBeAmended());
        reform.setIntendedOutcome(dto.getIntendedOutcome());
        reform.setPresentStatus(dto.getPresentStatus());
        reform.setStatus(dto.getStatus());

        // Clear old milestones (orphanRemoval ensures DB delete)
        reform.getMilestones().clear();

        // Add new milestones if present
        if (dto.getMilestones() != null && !dto.getMilestones().isEmpty()) {
            AtomicInteger counter = new AtomicInteger(1);

            dto.getMilestones().forEach(m -> {
                Document doc = (m.getDocumentId() != null)
                        ? documentRepository.findById(m.getDocumentId())
                        .orElseThrow(() -> new EntityNotFoundException("Document not found"))
                        : null;

                String suffix = String.format("%02d", counter.getAndIncrement());
                String milestoneId = reform.getEntityId() + "/" + suffix;

                ReformMilestone milestone = ReformMilestone.builder()
                        .entityId(milestoneId)
                        .activityDescription(m.getActivityDescription())
                        .deadline(m.getDeadline())
                        .document(doc)
                        .sortOrder(m.getSortOrder())
                        .reform(reform)
                        .build();

                reform.getMilestones().add(milestone);
            });
        }

        // Save reform — cascade saves milestones automatically
        RegulatoryReform updated = reformRepository.save(reform);

        return responseBuilder.mapToResponse(updated);
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
                .map(reform -> responseBuilder.mapToResponse(reform))
                .toList();
    }

}
