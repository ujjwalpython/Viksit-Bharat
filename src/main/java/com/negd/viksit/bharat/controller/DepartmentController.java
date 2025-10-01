package com.negd.viksit.bharat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.service.DepartmentService;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
	private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getAllDepartment() {
        return departmentService.getAllDepartment();
    }

}
