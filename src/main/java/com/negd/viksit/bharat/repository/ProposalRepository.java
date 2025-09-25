package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
