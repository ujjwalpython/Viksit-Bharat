package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.ReformMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReformMilestoneRepository extends JpaRepository<ReformMilestone, Long> {

    @Query(value = "SELECT COUNT(*) FROM reform_milestone", nativeQuery = true)
    Long countAllIncludingDeleted();
}

