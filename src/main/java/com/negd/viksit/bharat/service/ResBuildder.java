package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.*;
import com.negd.viksit.bharat.model.Goal;
import com.negd.viksit.bharat.model.ProjectScheme;
import com.negd.viksit.bharat.model.RegulatoryReform;
import com.negd.viksit.bharat.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResBuildder {

    public GoalResponseDto mapToDto(Goal goal, User user) {
        log.info("Mapping Goal entity to DTO: goalId={}, userId={}, roles={}",
                goal.getEntityId(), user.getEntityid(), user.getRoles());

        GoalResponseDto dto = new GoalResponseDto();

        String ministryStr = goal.getMinistry() != null ? goal.getMinistry().getName() : null;

  /*      if (user.hasRole("DEPT_ADMIN")) {
            log.info("User has DEPT_ADMIN role, checking department for ministry string.");
            if (user.getDepartment() != null) {
                ministryStr = ministryStr != null
                        ? ministryStr + " / " + user.getDepartment().getName()
                        : user.getDepartment().getName();
                log.info("Ministry string updated with department: {}", ministryStr);
            } else {
                log.info("User has DEPT_ADMIN role but no department assigned.");
            }
        } else {
            log.info("User does not have DEPT_ADMIN role. Using ministry only: {}", ministryStr);
        }
*/
        dto.setMinistryId(ministryStr);

        dto.setGoalId(goal.getEntityId());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setStatus(goal.getStatus());
        dto.setLastUpdate(goal.getUpdatedOn());
        log.info("Set basic goal fields: description='{}', status='{}', lastUpdate={}",
                goal.getGoalDescription(), goal.getStatus(), goal.getUpdatedOn());

        List<InterventionResDto> interventionDtos = goal.getInterventions() != null
                ? goal.getInterventions().stream().map(intervention -> {
            log.info("Mapping Intervention: id={}, target='{}'", intervention.getId(), intervention.getTargetDescription());
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
        }).collect(Collectors.toList())
                : List.of();

        dto.setInterventions(interventionDtos);
        log.info("Mapped {} interventions for goalId={}", interventionDtos.size(), goal.getEntityId());

        return dto;
    }

    public ProjectSchemeResponseDto mapToResponse(ProjectScheme project, User user) {
        String ministryStr = project.getMinistry() != null ? project.getMinistry().getName() : null;

/*
        if (user.hasRole("DEPT_ADMIN") && user.getDepartment() != null) {
            String departmentName = user.getDepartment().getName();
            if (ministryStr != null && !departmentName.isBlank()) {
                ministryStr = ministryStr + " / " + departmentName;
            }
        }
*/
        return ProjectSchemeResponseDto.builder()
                .id(project.getEntityId())
                .name(project.getName())
                .type(project.getType())
                .description(project.getDescription())
                .targetDate(project.getTargetDate())
                .totalBudgetRequired(project.getTotalBudgetRequired())
                .beneficiariesNo(project.getBeneficiariesNo())
                .status(project.getStatus())
                .ministry(ministryStr)
                .LastUpdate(project.getUpdatedOn())
                .keyDeliverables(project.getKeyDeliverables() != null
                                ? project.getKeyDeliverables().stream().map(k ->
                                        SchemeKeyDeliverableRespDto.builder()
                                                .id(k.getEntityId())
                                                .activityDescription(k.getActivityDescription())
                                                .deadline(k.getDeadline())
                                                .progressMade(k.getProgressMade())
                                                .documentId(k.getDocument() != null
                                                        ? k.getDocument().getId()
                                                        : null
                                                )
                                                .build()
                                )
                                .toList()
                                : List.of()
                )
                .build();
    }


    public RegulatoryRespReformDto mapToResponse(RegulatoryReform reform, User user) {
        String ministryStr = reform.getMinistry() != null ? reform.getMinistry().getName() : null;

/*
        if (user.hasRole("DEPT_ADMIN") && user.getDepartment() != null) {
            String departmentName = user.getDepartment().getName();
            if (ministryStr != null && !departmentName.isBlank()) {
                ministryStr = ministryStr + " / " + departmentName;
            }
        }
*/
        return RegulatoryRespReformDto.builder()
                .id(reform.getEntityId())
                .name(reform.getName())
                .description(reform.getDescription())
                .LastUpdate(reform.getUpdatedOn())
                .reformType(reform.getReformType())
                .ministry(ministryStr)
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

}
