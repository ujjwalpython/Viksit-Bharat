package com.negd.viksit.bharat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.KeyDeliverable;

@Repository
public interface KeyDeliverableRepository extends JpaRepository<KeyDeliverable, String> {
}
