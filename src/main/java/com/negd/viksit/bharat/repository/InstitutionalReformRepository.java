package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.InstitutionalReform;

@Repository
public interface InstitutionalReformRepository extends JpaRepository<InstitutionalReform, Long> {

	List<InstitutionalReform> findByCreatedBy(Long entityid);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCase(Long entityid, String status);

	List<InstitutionalReform> findByCreatedByAndNameContainingIgnoreCase(Long entityid, String description);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCaseAndNameContainingIgnoreCase(Long entityid,
			String status, String description);
}
