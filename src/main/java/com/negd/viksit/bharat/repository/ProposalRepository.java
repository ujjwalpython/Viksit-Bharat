package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.dto.OtherProposalDto;
import com.negd.viksit.bharat.model.OtherProposal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<OtherProposal, String> {

	List<OtherProposal> findByCreatedBy(Long entityid);

	List<OtherProposal> findByCreatedByAndStatusIgnoreCase(Long entityid, String status);

	List<OtherProposal> findByCreatedByAndProposalDescriptionContainingIgnoreCase(Long entityid, String proposalDescription);

	List<OtherProposal> findByCreatedByAndStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(Long entityid,
			String status, String proposalDescription);
}
