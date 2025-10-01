package com.negd.viksit.bharat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService {
	private final DepartmentRepository departmentRepository;

    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }
}
