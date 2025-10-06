package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.InstitutionalReform;

@Repository
public interface InstitutionalReformRepository extends JpaRepository<InstitutionalReform, String> {

	List<InstitutionalReform> findByCreatedBy(Long entityid);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCase(Long entityid, String status);

	List<InstitutionalReform> findByCreatedByAndReformDescriptionContainingIgnoreCase(Long entityid,
			String reformDescription);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(Long entityid,
			String status, String reformDescription);

	List<InstitutionalReform> findByStatusIgnoreCase(String status);

	List<InstitutionalReform> findByReformDescriptionContainingIgnoreCase(String reformDescription);

	List<InstitutionalReform> findByStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(String status,
			String reformDescription);
}
