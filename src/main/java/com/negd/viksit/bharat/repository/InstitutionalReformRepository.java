package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.InstitutionalReform;
import com.negd.viksit.bharat.model.PmInfrastructureReview;

@Repository
public interface InstitutionalReformRepository extends JpaRepository<InstitutionalReform, String> {
	List<InstitutionalReform> findAll(Sort sort);
	
	List<InstitutionalReform> findByCreatedBy(Long entityid, Sort sort);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCase(Long entityid, String status, Sort sort);

	List<InstitutionalReform> findByCreatedByAndReformDescriptionContainingIgnoreCase(Long entityid,
			String reformDescription, Sort sort);

	List<InstitutionalReform> findByCreatedByAndStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(Long entityid,
			String status, String reformDescription, Sort sort);

	List<InstitutionalReform> findByStatusIgnoreCase(String status, Sort sort);

	List<InstitutionalReform> findByReformDescriptionContainingIgnoreCase(String reformDescription, Sort sort);

	List<InstitutionalReform> findByStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(String status,
			String reformDescription, Sort sort);
}
