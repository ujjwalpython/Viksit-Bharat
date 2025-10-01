package com.negd.viksit.bharat.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.master.Ministry;

@Repository
public interface MinistryRepository extends JpaRepository<Ministry, UUID> {
	Optional<Ministry> findByCode(String code);
}

