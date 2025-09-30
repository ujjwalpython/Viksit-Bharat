package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.exception.InvalidStatusException;
import com.negd.viksit.bharat.model.Goal;
import com.negd.viksit.bharat.model.GoalIntervention;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.GoalRepository;
import com.negd.viksit.bharat.repository.InterventionRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final MinistryRepository ministryRepository;
    private final InterventionRepository interventionRepository;

    public GoalService(GoalRepository goalRepository, MinistryRepository ministryRepository, InterventionRepository interventionRepository) {
        this.goalRepository = goalRepository;
        this.ministryRepository = ministryRepository;
        this.interventionRepository = interventionRepository;
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

    public List<GoalResponseDto> getAllGoals() {
        return goalRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public GoalResponseDto getGoalById(String id) {
        return goalRepository.findByEntityId(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
    }

    @Transactional
    public GoalResponseDto updateGoal(String id, GoalDto goalDto) {
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

        return mapToDto(updated);
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

    private GoalResponseDto mapToDto(Goal goal) {
        GoalResponseDto dto = new GoalResponseDto();
        dto.setGoalId(goal.getEntityId());
        dto.setMinistryId(goal.getMinistry().getName());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setStatus(goal.getStatus());
        dto.setLastUpdate(goal.getUpdatedOn());

        List<InterventionResDto> interventionDtos = goal.getInterventions()
                .stream()
                .map(intervention -> {
                    InterventionResDto iDto = new InterventionResDto();
                    iDto.setId(intervention.getId());
                    iDto.setTargetDescription(intervention.getTargetDescription());

                    iDto.setPresentValue(intervention.getPresentValue());
                    iDto.setPresentUnit(intervention.getPresentUnit());
                    iDto.setPresentYear(intervention.getPresentYear());

                    iDto.setTarget2030Value(intervention.getTarget2030Value());
                    iDto.setTarget2030Unit(intervention.getTarget2030Unit());

                    iDto.setTarget2047Value(intervention.getTarget2047Value());
                    iDto.setTarget2047Unit(intervention.getTarget2047Unit());

                    iDto.setSortOrder(intervention.getSortOrder());
                    return iDto;
                }).collect(Collectors.toList());

        dto.setInterventions(interventionDtos);
        return dto;
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
        return mapToDto(saved);
    }

    public List<GoalResponseDto> filterGoals(Long entityid, String status, String goalDescription) {

        List<Goal> goals;
        if (status == null && goalDescription == null) {
            goals = goalRepository.findByCreatedBy(entityid);
        } else if (status != null && goalDescription == null) {
            goals = goalRepository.findByCreatedByAndStatusIgnoreCase(entityid, status);
        } else if (status == null) {
            goals = goalRepository.findByCreatedByAndGoalDescriptionContainingIgnoreCase(entityid, goalDescription);
        } else {
            goals = goalRepository.findByCreatedByAndStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(
                    entityid, status, goalDescription
            );
        }

        return goals.stream().map(goal ->mapToDto(goal)).toList();
    }
}
