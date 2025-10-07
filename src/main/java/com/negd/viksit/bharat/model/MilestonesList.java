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
@Table(name = "pm_infrastructure_review_sub_table",schema = "vb_core")
public class MilestonesList extends Auditable<Long>{

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(nullable = false)
	private String activityDescription;
	private LocalDate deadline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pm_Infrastructure_Review_id", nullable = false)
	private PmInfrastructureReview pmInfrastructureReview;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "document_id")
	private Document document;

}

