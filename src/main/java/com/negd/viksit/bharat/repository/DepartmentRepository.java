package com.negd.viksit.bharat.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.negd.viksit.bharat.model.master.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

}
