package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.RegulatoryReform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegulatoryReformRepository extends JpaRepository<RegulatoryReform, Long> {
    List<RegulatoryReform> findByCreatedBy(Long createdBy);

    List<RegulatoryReform> findByCreatedByAndStatusIgnoreCase(Long createdBy, String status);

    @Query(value = "SELECT COUNT(*) FROM regulatory_reform", nativeQuery = true)
    Long countAllIncludingDeleted();

    Optional<RegulatoryReform> findByEntityId(String entityId);
}
