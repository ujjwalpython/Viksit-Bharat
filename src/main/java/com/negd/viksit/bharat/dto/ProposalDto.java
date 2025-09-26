package com.negd.viksit.bharat.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalDto {
    private String id;
    private String goalId;
    private String ideaProposalTitle;
    private String proposalDescription;
    private String proposalType;
    private String potentialEconomicDevelopment;
    private String potentialEmploymentGeneration;
    private LocalDate timelineStart;
    private LocalDate timelineEnd;
    private String status;

}
