package com.negd.viksit.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReformMilestoneDto {
    private String id;
    private String activityDescription;
    private LocalDate deadline;
    private Integer sortOrder;
}

