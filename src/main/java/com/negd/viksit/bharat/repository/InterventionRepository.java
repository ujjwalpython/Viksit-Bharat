package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InterventionRepository extends JpaRepository<Intervention, UUID> {
}

