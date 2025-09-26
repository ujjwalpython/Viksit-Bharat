package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.RegulatoryReform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RegulatoryReformRepository extends JpaRepository<RegulatoryReform, String> {
    List<RegulatoryReform> findByCreatedBy(Long createdBy);
    List<RegulatoryReform> findByCreatedByAndStatusIgnoreCase(Long createdBy, String status);
}
