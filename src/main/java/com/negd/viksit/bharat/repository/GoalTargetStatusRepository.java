package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.GoalTargetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GoalTargetStatusRepository extends JpaRepository<GoalTargetStatus, UUID> {
    List<GoalTargetStatus> findByCreatedBy(Long entityId);

    List<GoalTargetStatus> findByCreatedByAndStatusIgnoreCase(Long entityId, String status);
}
