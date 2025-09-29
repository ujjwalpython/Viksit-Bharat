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
@Table(name = "target_interventions", schema = "vb_core")
public class TargetIntervention extends Auditable<Long> {

	@Id
	private String id;
//	private String goalId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ministry_id", nullable = false)
	private Ministry ministry;

	private String targetDetails;
	private String actionPoint;
	private LocalDate targetDate;

//    @Enumerated(EnumType.STRING)
//    private Priority priority;
	private String priority;

//    @Enumerated(EnumType.STRING)
//    private PresentStatus presentStatus;
	private String presentStatus;

//    @Enumerated(EnumType.STRING)
//    private ImplementationStatus implementationStatus;

	private String bottlenecks;

	@Column(name = "status", nullable = false)
	private String status;

	@OneToMany(mappedBy = "targetIntervention", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<KeyDeliverable> keyDeliverables = new ArrayList<>();

	public void addKeyDeliverable(KeyDeliverable deliverable) {
		deliverable.setTargetIntervention(this);
		this.keyDeliverables.add(deliverable);
	}

	public void removeKeyDeliverable(KeyDeliverable deliverable) {
		deliverable.setTargetIntervention(null);
		this.keyDeliverables.remove(deliverable);
	}

}
