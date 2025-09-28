package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OtherProposalDto {
	private String id;
//    private String goalId;
	private UUID ministryId;
	private String ideaProposalTitle;
	private String proposalDescription;
	private String proposalType;
	private String potentialEconomicDevelopment;
	private String potentialEmploymentGeneration;
	private LocalDate timelineStart;
	private LocalDate timelineEnd;
	private LocalDateTime lastUpdated;
	private String status;

}
