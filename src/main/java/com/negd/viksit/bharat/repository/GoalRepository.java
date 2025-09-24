package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
}

