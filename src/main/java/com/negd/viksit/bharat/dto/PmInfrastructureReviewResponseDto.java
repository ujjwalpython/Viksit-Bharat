package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PmInfrastructureReviewResponseDto {
	private String id;
	private String ministryId;
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(pattern = "dd MMM yyyy, HH:mm")
	private LocalDateTime lastUpdated;
	
	private String primatryMinistryDepartment;
	private LocalDate dateOfReviewMeeting;
	private String supportingMinistryDepartment;
	private String category;
	private LocalDate targetCompletionDate;
	private String actionItem;
	private String priority;
	private String presentStatusOfAchievement;
	private String bottlenecks;
	private String expectedOutcome;
	private String status;

	private List<MilestonesListDto> milestonesList;
}
