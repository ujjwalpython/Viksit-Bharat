package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.SchemeKeyDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeKeyDeliverableRepository extends JpaRepository<SchemeKeyDeliverable, String> {
}