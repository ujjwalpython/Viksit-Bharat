package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.Constants;
import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.exception.InvalidStatusException;
import com.negd.viksit.bharat.model.Goal;
import com.negd.viksit.bharat.model.GoalIntervention;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.GoalRepository;
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

    private final GoalRepository goalRepository;
    private final MinistryRepository ministryRepository;
    private final InterventionRepository interventionRepository;
    private final ResBuildder responseBuilder;

    public GoalService(GoalRepository goalRepository, MinistryRepository ministryRepository, InterventionRepository interventionRepository, ResBuildder responseBuilder) {
        this.goalRepository = goalRepository;
        this.ministryRepository = ministryRepository;
        this.interventionRepository = interventionRepository;
        this.responseBuilder = responseBuilder;
    }

    public GoalStatusRespDto createGoal(GoalDto goalDto) {
        Goal goal = new Goal();
        goal.setGoalDescription(goalDto.getGoalDescription());
        goal.setStatus(goalDto.getStatus());

        Ministry ministry = ministryRepository.findById(goalDto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));
        goal.setMinistry(ministry);

        goal.setInterventions(new ArrayList<>());

        Goal savedGoal = goalRepository.save(goal);

        AtomicInteger counter = new AtomicInteger(1);
        List<GoalIntervention> interventions = goalDto.getInterventions()
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
                    intervention.setGoal(savedGoal);

                    String suffix = String.format("%02d", counter.getAndIncrement());
                    intervention.setId(savedGoal.getEntityId() + "/" + suffix);

                    return intervention;
                })
                .toList();

        interventionRepository.saveAll(interventions);

        return new GoalStatusRespDto(savedGoal.getEntityId(), savedGoal.getStatus());
    }

    public List<GoalResponseDto> getAllGoals(User user) {
        return goalRepository.findAll()
                .stream()
                .map(goal -> responseBuilder.mapToDto(goal,user))
                .collect(Collectors.toList());
    }

    public GoalResponseDto getGoalById(String id,User user) {
        return goalRepository.findByEntityId(id)
                .map(goal -> responseBuilder.mapToDto(goal,user))
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
    }

    @Transactional
    public GoalResponseDto updateGoal(String id, GoalDto goalDto,User user) {
        Goal goal = goalRepository.findByEntityId(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));

        Ministry ministry = ministryRepository.findById(goalDto.getMinistryId())
                .orElseThrow(() -> new EntityNotFoundException("Ministry not found"));

        goal.setMinistry(ministry);
        goal.setGoalDescription(goalDto.getGoalDescription());
        goal.setStatus(goalDto.getStatus());

        interventionRepository.deleteByGoal(goal);

        AtomicInteger counter = new AtomicInteger(1);
        List<GoalIntervention> interventions = goalDto.getInterventions()
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

                    String suffix = String.format("%02d", counter.getAndIncrement());
                    intervention.setId(goal.getEntityId() + "/" + suffix);

                    return intervention;
                })
                .toList();

        interventionRepository.saveAll(interventions);

        Goal updated = goalRepository.save(goal);

        return responseBuilder.mapToDto(updated,user);
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

    public GoalResponseDto updateStatus(String id, String status,User user) {
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
        return responseBuilder.mapToDto(saved,user);
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
                .map(goal -> responseBuilder.mapToDto(goal,user))
                .filter(Objects::nonNull)
                .toList();
    }
}
