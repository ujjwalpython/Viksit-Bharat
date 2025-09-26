package com.negd.viksit.bharat.dto;

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
public class RegulatoryReformDto {
    private String id;
    private String name;
    private String description;
    private String reformType;
    private LocalDate targetCompletionDate;
    private String rulesToBeAmended;
    private String intendedOutcome;
    private String presentStatus;
    private String status;
    private List<ReformMilestoneDto> milestones;
}

