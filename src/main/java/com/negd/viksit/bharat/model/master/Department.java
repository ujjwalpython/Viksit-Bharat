package com.negd.viksit.bharat.model.master;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.Column;
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
@Table(name = "department", schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends Auditable<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true, length = 20)
	private String code;

	@Column(nullable = false, unique = true, length = 200)
	private String name;

	@Column(length = 500)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ministry_id", nullable = false)
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Ministry ministry;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;


}
