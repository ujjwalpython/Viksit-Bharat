package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByCreatedBy(Long createdBy);

    List<Goal> findByCreatedByAndStatusIgnoreCase(Long createdBy, String status);

    List<Goal> findByCreatedByAndGoalDescriptionContainingIgnoreCase(Long createdBy, String goalDescription);

    List<Goal> findByCreatedByAndStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(
            Long createdBy, String status, String goalDescription
    );

    @Query(value = "SELECT COUNT(*) FROM vb_core.vb_goals", nativeQuery = true)
    Long countAllIncludingDeleted();

    Optional<Goal> findByEntityId(String entityId);

    List<Goal> findByStatusIgnoreCase(String status);

    List<Goal> findByGoalDescriptionContainingIgnoreCase(String goalDescription);

    List<Goal> findByStatusIgnoreCaseAndGoalDescriptionContainingIgnoreCase(String status, String goalDescription);
}

