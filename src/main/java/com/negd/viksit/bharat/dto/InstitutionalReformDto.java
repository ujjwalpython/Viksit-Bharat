package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionalReformDto {

	private String id;
	
	private String goalId;
	
	private String institutionalReformName;

	private String reformDescription;

//	private ReformType reformType;
	private String reformType;

	private LocalDate targetCompletionDate;

	private String rulesToBeAmended;

	private String intendedOutcome;

	private String presentStatus;

	private String status;
	
	private List<TargetDto> target;
}
