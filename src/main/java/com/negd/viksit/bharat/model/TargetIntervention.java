package com.negd.viksit.bharat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "target_interventions",schema = "vb_core")
public class TargetIntervention extends Auditable<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String targetDetails;
	private String actionPoint;
	private LocalDate targetDate;
    private String priority; 
    private String presentStatus; 
    private String implementationStatus; 
    private String bottlenecks;
    
    private String formStatus;

	@OneToMany(mappedBy = "targetIntervention", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<KeyDeliverable> keyDeliverables = new ArrayList<>();

	// Getters and Setters
	public void addKeyDeliverable(KeyDeliverable deliverable) {
		deliverable.setTargetIntervention(this);
		this.keyDeliverables.add(deliverable);
	}

	public void removeKeyDeliverable(KeyDeliverable deliverable) {
		deliverable.setTargetIntervention(null);
		this.keyDeliverables.remove(deliverable);
	}

}
