package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.RegulatoryReformDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.RegulatoryReformService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regulatory-reforms")
@RequiredArgsConstructor
public class RegulatoryReformController {

    private final RegulatoryReformService reformService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RegulatoryReformDto dto, HttpServletRequest request) {
        RegulatoryReformDto created = reformService.createReform(dto);
        return ResponseGenerator.created(created, "Reform created successfully", request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id, HttpServletRequest request) {
        RegulatoryReformDto reform = reformService.getReform(id);
        return ResponseGenerator.success(reform, "Reform fetched successfully", request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestBody RegulatoryReformDto dto,
                                    HttpServletRequest request) {
        RegulatoryReformDto updated = reformService.updateReform(id, dto);
        return ResponseGenerator.success(updated, "Reform updated successfully", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
        reformService.deleteReform(id);
        return ResponseGenerator.success(null, "Reform deleted successfully", request);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<RegulatoryReformDto> all = reformService.getAllReforms();
        return ResponseGenerator.success(all, "All reforms fetched successfully", request);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@AuthenticationPrincipal User user,
                                    @RequestParam(required = false) String status,
                                    HttpServletRequest request) {
        List<RegulatoryReformDto> reforms = reformService.filterReforms(user.getEntityid(), status);
        return ResponseGenerator.success(reforms, "Filtered reforms fetched successfully", request);
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable String id,
                                          @PathVariable String status,
                                          HttpServletRequest request) {
        RegulatoryReformDto updated = reformService.updateStatus(id, status);
        return ResponseGenerator.success(updated, "Reform status updated successfully", request);
    }
}

