package com.negd.viksit.bharat.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class KeyDeliverableDto {
	private Long id;  
	private String activityDescription;
	private LocalDate deadline;
	private String progressMade;
	private String documentPath;
	
}
