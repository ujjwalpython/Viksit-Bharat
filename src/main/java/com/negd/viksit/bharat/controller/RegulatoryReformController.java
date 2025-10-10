package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.ReformMilestoneRespDto;
import com.negd.viksit.bharat.dto.RegulatoryReformDto;
import com.negd.viksit.bharat.dto.RegulatoryRespReformDto;
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
    public ResponseEntity<?> create(@AuthenticationPrincipal User user,@RequestBody RegulatoryReformDto dto, HttpServletRequest request) {
        RegulatoryRespReformDto created = reformService.createReform(dto);
        return ResponseGenerator.created(created, "Reform created successfully", request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@AuthenticationPrincipal User user,@PathVariable String id, HttpServletRequest request) {
        RegulatoryRespReformDto reform = reformService.getReformById(id);
        return ResponseGenerator.success(reform, "Reform fetched successfully", request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@AuthenticationPrincipal User user,@PathVariable String id,
                                    @RequestBody RegulatoryReformDto dto,
                                    HttpServletRequest request) {
        RegulatoryRespReformDto updated = reformService.updateReform(id, dto);
        return ResponseGenerator.success(updated, "Reform updated successfully", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
        reformService.deleteReform(id);
        return ResponseGenerator.success(null, "Reform deleted successfully", request);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request,@AuthenticationPrincipal User user) {
        List<RegulatoryRespReformDto> all = reformService.getAllReforms();
        return ResponseGenerator.success(all, "All reforms fetched successfully", request);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterReforms(@AuthenticationPrincipal User user,
                                           @RequestParam(required = false) String status,
                                           HttpServletRequest request) {

        List<RegulatoryRespReformDto> reforms = reformService.filterReforms(user, status);
        return ResponseGenerator.success(reforms, "Reforms fetched successfully", request);
    }

}

