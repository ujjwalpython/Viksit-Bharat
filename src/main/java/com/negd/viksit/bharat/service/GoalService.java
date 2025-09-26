package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.GoalDto;
import com.negd.viksit.bharat.dto.GoalRespDto;
import com.negd.viksit.bharat.dto.InterventionDto;
import com.negd.viksit.bharat.exception.InvalidStatusException;
import com.negd.viksit.bharat.model.Goal;
import com.negd.viksit.bharat.model.GoalIntervention;
import com.negd.viksit.bharat.repository.GoalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public GoalRespDto createGoal(GoalDto goalDto) {
        Goal goal = mapToEntity(goalDto);
        Goal saved = goalRepository.save(goal);
        return new GoalRespDto(saved.getId(),saved.getStatus());
    }

    public List<GoalDto> getAllGoals() {
        return goalRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public GoalDto getGoalById(String id) {
        return goalRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
    }

    public GoalDto updateGoal(String id, GoalDto goalDto) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setMinistryId(goalDto.getMinistryId());
        goal.setGoalDescription(goalDto.getGoalDescription());
        goal.setStatus(goalDto.getStatus());
        goal.getInterventions().clear();

        List<GoalIntervention> interventions = goalDto.getInterventions()
                .stream()
                .map(iDto -> {
                    GoalIntervention intervention = new GoalIntervention();
                    intervention.setTargetDescriptionId(iDto.getTargetDescriptionId());
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

        goal.getInterventions().addAll(interventions);

        Goal updated = goalRepository.save(goal);
        return mapToDto(updated);
    }

    public void deleteGoal(String id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Goal not found"));
        goalRepository.delete(goal);
    }

    private Goal mapToEntity(GoalDto dto) {
        Goal goal = new Goal();
        goal.setMinistryId(dto.getMinistryId());
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setStatus(dto.getStatus());

        List<GoalIntervention> interventions = dto.getInterventions()
                .stream()
                .map(iDto -> {
                    GoalIntervention intervention = new GoalIntervention();
                    intervention.setTargetDescriptionId(iDto.getTargetDescriptionId());
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

    private GoalDto mapToDto(Goal goal) {
        GoalDto dto = new GoalDto();
        dto.setGoalId(goal.getId());
        dto.setMinistryId(goal.getMinistryId());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setStatus(goal.getStatus());
        dto.setLastUpdate(goal.getUpdatedOn());

        List<InterventionDto> interventionDtos = goal.getInterventions()
                .stream()
                .map(intervention -> {
                    InterventionDto iDto = new InterventionDto();
                    iDto.setTargetDescriptionId(intervention.getTargetDescriptionId());
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

    public GoalDto updateStatus(String id, String status) {
        Goal goal = goalRepository.findById(id)
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

    public List<GoalDto> filterGoals(Long entityid, String status, String goalDescription) {

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
