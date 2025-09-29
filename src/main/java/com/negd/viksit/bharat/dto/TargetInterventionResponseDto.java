package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TargetInterventionResponseDto {
	private String id;
//	private String goalId;
	private String ministryId;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(pattern = "dd MMM yyyy, HH:mm")
	private LocalDateTime lastUpdated;

	private String targetDetails;
	private String actionPoint;
	private LocalDate targetDate;
	private String priority;
	private String presentStatus;
//	private Priority priority;
//	private PresentStatus presentStatus;
//	private ImplementationStatus implementationStatus;
	private String bottlenecks;
	private String status;
	private List<KeyDeliverableDto> keyDeliverables;
}
