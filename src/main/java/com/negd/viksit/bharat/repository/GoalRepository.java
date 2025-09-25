package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, String> {

    List<Goal> findByCreatedBy(Long createdBy);

    // Goals by user and status
    List<Goal> findByCreatedByAndStatusIgnoreCase(Long createdBy, String status);

    // Goals by user and description
    List<Goal> findByCreatedByAndGoalDescriptionContainingIgnoreCase(Long createdBy, String goalDescription);

    // Goals by user, status, and description
    List<Goal> findByCreatedByAndStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(
            Long createdBy, String status, String goalDescription
    );
    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(g.id, 7) AS int)), 0) FROM Goal g")
    long getMaxGoalNumber();
}

