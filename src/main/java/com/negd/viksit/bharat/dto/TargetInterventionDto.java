package com.negd.viksit.bharat.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.negd.viksit.bharat.model.KeyDeliverable;

import lombok.Data;

@Data
public class TargetInterventionDto {
	private String targetDetails;
	private String actionPoint;
	private LocalDate targetDate;
	private List<KeyDeliverableDto> keyDeliverables; 
}
