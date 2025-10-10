package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.ProjectSchemeDto;
import com.negd.viksit.bharat.dto.ProjectSchemeResponseDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.ProjectSchemeService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectSchemeController {

    private final ProjectSchemeService projectService;


    public ProjectSchemeController(ProjectSchemeService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user,@RequestBody ProjectSchemeDto dto, HttpServletRequest request) {
        ProjectSchemeResponseDto created = projectService.create(dto);
        return ResponseGenerator.created(created, "Project Scheme created successfully", request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id, HttpServletRequest request,@AuthenticationPrincipal User user) {
        ProjectSchemeResponseDto project = projectService.getById(id);
        return ResponseGenerator.success(project, "Project Scheme fetched successfully", request);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request,@AuthenticationPrincipal User user) {
        List<ProjectSchemeResponseDto> projects = projectService.getAll(user);
        return ResponseGenerator.success(projects, "Project Schemes fetched successfully", request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,@PathVariable String id,
                                    @RequestBody ProjectSchemeDto dto,
                                    HttpServletRequest request) {
        ProjectSchemeResponseDto updated = projectService.update(id, dto,user);
        return ResponseGenerator.success(updated, "Project Scheme updated successfully", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
        projectService.delete(id);
        return ResponseGenerator.success(null, "Project Scheme deleted successfully", request);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterProjects(@AuthenticationPrincipal User user,
                                            @RequestParam(required = false) String status,
                                            HttpServletRequest request) {

        List<ProjectSchemeResponseDto> projects = projectService.filterProjects(user, status);
        return ResponseGenerator.success(projects, "Projects fetched successfully", request);
    }

}




