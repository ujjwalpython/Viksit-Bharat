package com.negd.viksit.bharat.dto;

import java.time.LocalDate;

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
	private LocalDate deadline;
	private String documentPath;
	private String activityDescription;

}
