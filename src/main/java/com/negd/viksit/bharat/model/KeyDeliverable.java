package com.negd.viksit.bharat.model;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "KeyDeliverable")
public class KeyDeliverable extends Auditable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String activityDescription;
	private LocalDate deadline;
	private String progressMade;
	private String documentPath; // uploaded document path

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_intervention_id")
	private TargetIntervention targetIntervention;

	// getters and setters
}
