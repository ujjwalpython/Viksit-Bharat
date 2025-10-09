package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.TargetIntervention;

@Repository
public interface TargetInterventionRepository extends JpaRepository<TargetIntervention, String> {
	List<TargetIntervention>findAll(Sort sort);
	
	List<TargetIntervention> findByCreatedBy(Long entityId, Sort sort);

	List<TargetIntervention> findByCreatedByAndStatusIgnoreCase(Long entityId, String status, Sort sort);

	List<TargetIntervention> findByCreatedByAndTargetDetailsContainingIgnoreCase(Long entityId, String targetDetails, Sort sort);

	List<TargetIntervention> findByCreatedByAndStatusIgnoreCaseAndTargetDetailsContainingIgnoreCase(Long entityId,
			String status, String targetDetails, Sort sort);

	List<TargetIntervention> findByStatusIgnoreCase(String status, Sort sort);

	List<TargetIntervention> findByTargetDetailsContainingIgnoreCase(String targetDetails, Sort sort);

	List<TargetIntervention> findByStatusIgnoreCaseAndTargetDetailsContainingIgnoreCase(String status,
			String targetDetails, Sort sort);

}
