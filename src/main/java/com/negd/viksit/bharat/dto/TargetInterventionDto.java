package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.negd.viksit.bharat.enums.PresentStatus;
import com.negd.viksit.bharat.enums.Priority;

import lombok.Data;

@Data
public class TargetInterventionDto {
	private String id;
	private String goalId;
    private String ministry;           
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
