package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.ProjectScheme;
import com.negd.viksit.bharat.model.SchemeKeyDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeDeliverableRepository extends JpaRepository<SchemeKeyDeliverable,Long> {

    void deleteByProjectScheme(ProjectScheme project);
}
