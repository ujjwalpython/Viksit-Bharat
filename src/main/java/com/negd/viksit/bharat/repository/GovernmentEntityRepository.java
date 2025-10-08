package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.model.master.Ministry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GovernmentEntityRepository extends JpaRepository<GovernmentEntity, UUID> {
}

