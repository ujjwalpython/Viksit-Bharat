package com.negd.viksit.bharat.model;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "target_interventions_sub_table",schema = "vb_core")
public class KeyDeliverable extends Auditable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String activityDescription;
	private LocalDate deadline;
//	private String progressMade;
//	private String documentPath; 

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "target_intervention_id", nullable = false)
	private TargetIntervention targetIntervention;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "document_id", nullable = false)
	private Document document;

}
