package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.TargetIntervention;

@Repository
public interface TargetInterventionRepository extends JpaRepository<TargetIntervention, String> {

	List<TargetIntervention> findByCreatedBy(Long entityId);

	List<TargetIntervention> findByCreatedByAndStatusIgnoreCase(Long entityId, String status);

	List<TargetIntervention> findByCreatedByAndTargetDetailsContainingIgnoreCase(Long entityId, String targetDetails);

	List<TargetIntervention> findByCreatedByAndStatusIgnoreCaseAndTargetDetailsContainingIgnoreCase(Long entityId,
			String status, String targetDetails);

	List<TargetIntervention> findByStatusIgnoreCase(String status);

	List<TargetIntervention> findByTargetDetailsContainingIgnoreCase(String targetDetails);

	List<TargetIntervention> findByStatusIgnoreCaseAndTargetDetailsContainingIgnoreCase(String status,
			String targetDetails);

}
