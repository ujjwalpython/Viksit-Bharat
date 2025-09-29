package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TargetDto {

	private Long id;
	private String activityDescription;
	private LocalDate deadline;
//	private String documentPath;
	
	private UUID documentId;
	private String documentUrl;

}
