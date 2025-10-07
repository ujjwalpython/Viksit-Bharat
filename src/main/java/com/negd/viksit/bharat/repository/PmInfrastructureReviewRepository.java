package com.negd.viksit.bharat.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.PmInfrastructureReview;

@Repository
public interface PmInfrastructureReviewRepository extends JpaRepository<PmInfrastructureReview, String> {
	List<PmInfrastructureReview> findAll(Sort sort);

	List<PmInfrastructureReview> findByCreatedBy(Long entityId, Sort sort);

	List<PmInfrastructureReview> findByStatusIgnoreCase(String status, Sort sort);

	List<PmInfrastructureReview> findByActionItemContainingIgnoreCase(String actionItem, Sort sort);

	List<PmInfrastructureReview> findByStatusIgnoreCaseAndActionItemContainingIgnoreCase(String status,
			String actionItem, Sort sort);

	List<PmInfrastructureReview> findByCreatedByAndStatusIgnoreCase(Long entityId, String status, Sort sort);

	List<PmInfrastructureReview> findByCreatedByAndActionItemContainingIgnoreCase(Long entityId, String actionItem,
			Sort sort);

	List<PmInfrastructureReview> findByCreatedByAndStatusIgnoreCaseAndActionItemContainingIgnoreCase(Long entityId,
			String status, String actionItem, Sort sort);

}
