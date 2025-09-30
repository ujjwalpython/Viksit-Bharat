package com.negd.viksit.bharat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class GoalDto {
    private String goalId;
    private UUID ministryId;
    private String goalDescription;
    private String status;

    private List<InterventionDto> interventions;

}
