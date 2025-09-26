package com.negd.viksit.bharat.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionalReformDto {

    private Long id;

    private String name;

    private String description;

    private String reformType;

    private LocalDate targetCompletionDate;

    private String rulesToBeAmended;

    private String intendedOutcome;

    private String presentStatus;

    private Long targetId;  
    
    private TargetDto target; 
    
    private String status;
}
