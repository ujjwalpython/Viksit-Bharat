package com.negd.viksit.bharat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegulatoryRespReformDto {
    private String id;
    private String name;
    private String description;
    private String reformType;
    private String ministry;
    private LocalDate targetCompletionDate;
    private String rulesToBeAmended;
    private String intendedOutcome;
    private String presentStatus;
    private String status;

    @JsonFormat(pattern = "dd MMM yyyy, HH:mm")
    private LocalDateTime LastUpdate;

    private List<ReformMilestoneRespDto> milestones;
}

