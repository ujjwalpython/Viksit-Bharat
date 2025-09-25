package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.master.TargetIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TargetIndicatorRepository extends JpaRepository<TargetIndicator, UUID> {
}
