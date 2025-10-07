package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class MilestonesListDto {
	private String id;
	private String activityDescription;
	private LocalDate deadline;	
	private UUID documentId;
	private String documentUrl;
}
