package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.GoalTargetStatusDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.GoalTargetStatusService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goal-target-status")
@RequiredArgsConstructor
public class GoalTargetStatusController {

    private final GoalTargetStatusService goalTargetStatusService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GoalTargetStatusDto dto, HttpServletRequest request) {
        GoalTargetStatusDto created = goalTargetStatusService.createGoalTargetStatus(dto);
        return ResponseGenerator.created(created, "Target status created successfully", request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id, HttpServletRequest request) {
        GoalTargetStatusDto goalTargetStatus = goalTargetStatusService.getGoalTargetStatus(id);
        return ResponseGenerator.success(goalTargetStatus, "Target status fetched successfully", request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id,
                                    @RequestBody GoalTargetStatusDto dto,
                                    HttpServletRequest request) {
        GoalTargetStatusDto updated = goalTargetStatusService.updateGoalTargetStatus(id, dto);
        return ResponseGenerator.success(updated, "Target status updated successfully", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, HttpServletRequest request) {
        goalTargetStatusService.deleteGoalTargetStatus(id);
        return ResponseGenerator.success(null, "Target status deleted successfully", request);
    }

    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<GoalTargetStatusDto> allStatuses = goalTargetStatusService.getAllTargetStatuses();
        return ResponseGenerator.success(allStatuses, "All target statuses fetched successfully", request);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterTargetStatuses(@AuthenticationPrincipal User user,
                                                  @RequestParam(required = false) String status,
                                                  HttpServletRequest request) {

        List<GoalTargetStatusDto> statuses = goalTargetStatusService.filterTargetStatuses(
                user.getEntityid(), status);

        return ResponseGenerator.success(statuses, "Target statuses fetched successfully", request);
    }


    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable UUID id,
            @PathVariable String status,
            HttpServletRequest request) {

        GoalTargetStatusDto updated = goalTargetStatusService.updateStatus(id, status);
        return ResponseGenerator.success(updated, "Target status updated successfully", request);
    }

}
