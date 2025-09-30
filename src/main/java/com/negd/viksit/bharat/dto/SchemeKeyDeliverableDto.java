package com.negd.viksit.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeKeyDeliverableDto {
    private String activityDescription;
    private LocalDate deadline;
    private String progressMade;
    private UUID documentId;
}
