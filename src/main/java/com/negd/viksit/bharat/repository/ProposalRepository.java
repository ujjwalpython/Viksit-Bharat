package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.dto.OtherProposalDto;
import com.negd.viksit.bharat.model.OtherProposal;
import com.negd.viksit.bharat.model.PmInfrastructureReview;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<OtherProposal, String> {

	List<OtherProposal> findAll(Sort sort);

	List<OtherProposal> findByCreatedBy(Long entityid, Sort sort);

	List<OtherProposal> findByCreatedByAndStatusIgnoreCase(Long entityid, String status, Sort sort);

	List<OtherProposal> findByCreatedByAndProposalDescriptionContainingIgnoreCase(Long entityid,
			String proposalDescription, Sort sort);

	List<OtherProposal> findByCreatedByAndStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(Long entityid,
			String status, String proposalDescription, Sort sort);

	List<OtherProposal> findByStatusIgnoreCase(String status, Sort sort);

	List<OtherProposal> findByProposalDescriptionContainingIgnoreCase(String description, Sort sort);

	List<OtherProposal> findByStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(String status,
			String description, Sort sort);

}
