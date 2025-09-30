package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.ProjectScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectSchemeRepository extends JpaRepository<ProjectScheme,String> {

    List<ProjectScheme> findByCreatedBy(Long createdBy);

    List<ProjectScheme> findByCreatedByAndStatusIgnoreCase(Long createdBy, String status);

    Optional<ProjectScheme> findByEntityId(String id);
}
