package com.negd.viksit.bharat.model;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institutional_reform_sub_table", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Target extends Auditable<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	private String name;
	private LocalDate deadline;
//	private String documentPath;
	private String activityDescription;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "institutional_reform_id")
	private InstitutionalReform institutionalReform;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "document_id", nullable = false)
	private Document document;
}
