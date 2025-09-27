package com.negd.viksit.bharat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class GoalResponseDto {
    private String goalId;
    private String ministryId;
    private String goalDescription;
    private String status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "dd MMM yyyy, HH:mm")
    private LocalDateTime LastUpdate;

    private List<InterventionDto> interventions;
}
