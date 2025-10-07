package com.negd.viksit.bharat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.Ministry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pm_infrastructure_review", schema = "vb_core")
public class PmInfrastructureReview extends Auditable<Long>{

	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ministry_id", nullable = false)
	private Ministry ministry;
	
	private String primatryMinistryDepartment;
	private LocalDate dateOfReviewMeeting;
	private String supportingMinistryDepartment;
	private String category;
	private LocalDate targetCompletionDate;
	private String actionItem;
	private String priority;
	private String presentStatusOfAchievement;
	private String bottlenecks;
	private String expectedOutcome;

	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "pmInfrastructureReview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MilestonesList> milestonesList = new ArrayList<>();

	public void addMilestonesList(MilestonesList deliverable) {
		deliverable.setPmInfrastructureReview(this);
		this.milestonesList.add(deliverable);
	}

	public void removeMilestonesList(MilestonesList deliverable) {
		deliverable.setPmInfrastructureReview(null);
		this.milestonesList.remove(deliverable);
	}

}
