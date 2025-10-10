package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.exception.InvalidStatusException;
import com.negd.viksit.bharat.model.Goal;
import com.negd.viksit.bharat.model.GoalIntervention;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.repository.GoalRepository;
import com.negd.viksit.bharat.repository.GovernmentEntityRepository;
import com.negd.viksit.bharat.repository.InterventionRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
public class GoalService {
    private final GovernmentEntityRepository governmentEntityRepository;

    private final GoalRepository goalRepository;
    private final InterventionRepository interventionRepository;
    private final ResBuildder responseBuilder;

    public GoalService(GoalRepository goalRepository, MinistryRepository ministryRepository, InterventionRepository interventionRepository, ResBuildder responseBuilder,
                       GovernmentEntityRepository governmentEntityRepository) {
        this.goalRepository = goalRepository;
        this.interventionRepository = interventionRepository;
        this.responseBuilder = responseBuilder;
        this.governmentEntityRepository = governmentEntityRepository;
    }

    @Transactional
    public GoalStatusRespDto createGoal(GoalDto goalDto) {

        GovernmentEntity governmentEntity = governmentEntityRepository.findById(goalDto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        Goal goal = new Goal();
        goal.setGoalDescription(goalDto.getGoalDescription());
        goal.setStatus(goalDto.getStatus());
        goal.setGovernmentEntity(governmentEntity);
        goal.setInterventions(new ArrayList<>());

        // Persist goal first to get its seqNum (for entityId generation)
        Goal savedGoal = goalRepository.saveAndFlush(goal);

        // Now that entityId is available, we can safely build interventions
        AtomicInteger counter = new AtomicInteger(1);
        goalDto.getInterventions().forEach(iDto -> {
            GoalIntervention intervention = new GoalIntervention();
            intervention.setTargetDescription(iDto.getTargetDescription());
            intervention.setPresentValue(iDto.getPresentValue());
            intervention.setPresentUnit(iDto.getPresentUnit());
            intervention.setPresentYear(iDto.getPresentYear());
            intervention.setTarget2030Value(iDto.getTarget2030Value());
            intervention.setTarget2030Unit(iDto.getTarget2030Unit());
            intervention.setTarget2047Value(iDto.getTarget2047Value());
            intervention.setTarget2047Unit(iDto.getTarget2047Unit());
            intervention.setSortOrder(iDto.getSortOrder());
            intervention.setGoal(savedGoal);

            String suffix = String.format("%02d", counter.getAndIncrement());
            intervention.setEntityId(savedGoal.getEntityId() + "/" + suffix);

            savedGoal.getInterventions().add(intervention);
        });

        // Save goal again to cascade interventions
        goalRepository.save(savedGoal);

        return new GoalStatusRespDto(savedGoal.getEntityId(), savedGoal.getStatus());
    }

    public List<GoalResponseDto> getAllGoals(User user) {
        return goalRepository.findAll()
                .stream()
                .map(goal -> responseBuilder.mapToDto(goal))
                .collect(Collectors.toList());
    }

    public GoalResponseDto getGoalById(String id,User user) {
        return goalRepository.findByEntityId(id)
                .map(goal -> responseBuilder.mapToDto(goal))
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
    }

    @Transactional
    public GoalResponseDto updateGoal(String id, GoalDto goalDto) {
        Goal goal = goalRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        GovernmentEntity governmentEntity = governmentEntityRepository.findById(goalDto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry/Department not found"));

        goal.setGovernmentEntity(governmentEntity);
        goal.setGoalDescription(goalDto.getGoalDescription());
        goal.setStatus(goalDto.getStatus());

        // Clear old interventions safely
        goal.getInterventions().clear();

        // Recreate new interventions and attach to goal
        AtomicInteger counter = new AtomicInteger(1);
        goalDto.getInterventions().forEach(iDto -> {
            GoalIntervention intervention = new GoalIntervention();
            intervention.setTargetDescription(iDto.getTargetDescription());
            intervention.setPresentValue(iDto.getPresentValue());
            intervention.setPresentUnit(iDto.getPresentUnit());
            intervention.setPresentYear(iDto.getPresentYear());
            intervention.setTarget2030Value(iDto.getTarget2030Value());
            intervention.setTarget2030Unit(iDto.getTarget2030Unit());
            intervention.setTarget2047Value(iDto.getTarget2047Value());
            intervention.setTarget2047Unit(iDto.getTarget2047Unit());
            intervention.setSortOrder(iDto.getSortOrder());
            intervention.setGoal(goal);

            String suffix = String.format("%02d", counter.getAndIncrement());
            intervention.setEntityId(goal.getEntityId() + "/" + suffix);

            goal.getInterventions().add(intervention);
        });

        // No need to manually delete/save interventions or call their repository
        Goal updated = goalRepository.save(goal);

        return responseBuilder.mapToDto(updated);
    }

    public void deleteGoal(String id) {
        Goal goal = goalRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
        goalRepository.delete(goal);
    }

    private Goal mapToEntity(GoalDto dto) {

        Goal goal = new Goal();
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setStatus(dto.getStatus());

        List<GoalIntervention> interventions = dto.getInterventions()
                .stream()
                .map(iDto -> {
                    GoalIntervention intervention = new GoalIntervention();
                    intervention.setTargetDescription(iDto.getTargetDescription());
                    intervention.setPresentValue(iDto.getPresentValue());
                    intervention.setPresentUnit(iDto.getPresentUnit());
                    intervention.setPresentYear(iDto.getPresentYear());
                    intervention.setTarget2030Value(iDto.getTarget2030Value());
                    intervention.setTarget2030Unit(iDto.getTarget2030Unit());
                    intervention.setTarget2047Value(iDto.getTarget2047Value());
                    intervention.setTarget2047Unit(iDto.getTarget2047Unit());
                    intervention.setSortOrder(iDto.getSortOrder());
                    intervention.setGoal(goal);
                    return intervention;
                }).collect(Collectors.toList());

        goal.setInterventions(interventions);
        return goal;
    }

    public GoalResponseDto updateStatus(String id, String status) {
        Goal goal = goalRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found with id: " + id));

        String newStatus = status.toUpperCase();
        switch (newStatus) {
            case "DRAFT":
            case "SUBMITTED":
            case "APPROVED":
            case "REJECTED":
                goal.setStatus(newStatus);
                break;
            default:
                throw new InvalidStatusException("Invalid status: " + newStatus);
        }

        Goal saved = goalRepository.save(goal);
        return responseBuilder.mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<GoalResponseDto> filterGoals(User user, String status, String goalDesc) {
        final Long id = user.getEntityid();
        final boolean scoped = Stream.of(Constants.MADMIN, Constants.DADMIN).anyMatch(user::hasRole);

        return Optional.ofNullable(
                        Stream.of(new Object())
                                .map(__ -> {
                                    Predicate<Object> n = Objects::nonNull;
                                    boolean s = n.test(status), g = n.test(goalDesc);

                                    return scoped
                                            ? s && g ? goalRepository.findByCreatedByAndStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(id, status, goalDesc)
                                            : s ? goalRepository.findByCreatedByAndStatusIgnoreCase(id, status)
                                            : g ? goalRepository.findByCreatedByAndGoalDescriptionContainingIgnoreCase(id, goalDesc)
                                            : goalRepository.findByCreatedBy(id)
                                            : s && g ? goalRepository.findByStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(status, goalDesc)
                                            : s ? goalRepository.findByStatusIgnoreCase(status)
                                            : g ? goalRepository.findByGoalDescriptionContainingIgnoreCase(goalDesc)
                                            : goalRepository.findAll();
                                })
                                .findFirst()
                                .orElse(Collections.emptyList())
                )
                .stream()
                .flatMap(Collection::stream)
                .map(goal -> responseBuilder.mapToDto(goal))
                .filter(Objects::nonNull)
                .toList();
    }
}
