package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class KeyDeliverableDto {
	private Long id;
	private String activityDescription;
	private LocalDate deadline;
//	private String progressMade;
	private String documentPath;

	private UUID documentId; // <-- Document का reference
	private String documentUrl; // Optional convenience field
}
