package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.dto.ProposalDto;
import com.negd.viksit.bharat.model.Proposal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, String> {

	List<Proposal> findByCreatedBy(Long entityid);

	List<Proposal> findByCreatedByAndStatusIgnoreCase(Long entityid, String status);

	List<Proposal> findByCreatedByAndProposalDescriptionContainingIgnoreCase(Long entityid, String proposalDescription);

	List<Proposal> findByCreatedByAndStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(Long entityid,
			String status, String proposalDescription);
}
