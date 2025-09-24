package com.negd.viksit.bharat.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GoalDto {
    private UUID ministryId;
    private String goalDescription;
    private String status;
    private List<InterventionDto> interventions;

}
