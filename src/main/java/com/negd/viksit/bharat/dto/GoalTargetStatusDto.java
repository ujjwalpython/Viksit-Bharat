package com.negd.viksit.bharat.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalTargetStatusDto {
    private UUID id;
    private String target;
    private String actionPoint;
    private String presentStatusOfAchievement;
    private String bottlenecks;
    private String priority;
    private String status;
    private LocalDate completionDate;
    private List<MilestoneDto> milestones;
}

