package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.GoalDto;
import com.negd.viksit.bharat.dto.GoalRespDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.GoalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<?> createGoal( @RequestBody GoalDto goalDto, HttpServletRequest request) {
        GoalRespDto createdGoal = goalService.createGoal(goalDto);
        return ResponseGenerator.created(createdGoal, "Goal created successfully", request);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable String id, HttpServletRequest request) {
        GoalDto goal = goalService.getGoalById(id);
        return ResponseGenerator.success(goal, "Goal fetched successfully", request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable String id, @RequestBody GoalDto goalDto, HttpServletRequest request) {
        GoalDto updatedGoal = goalService.updateGoal(id, goalDto);
        return ResponseGenerator.success(updatedGoal, "Goal updated successfully", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable String id, HttpServletRequest request) {
        goalService.deleteGoal(id);
        return ResponseGenerator.success(null, "Goal deleted successfully", request);
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @PathVariable String status,
            HttpServletRequest request){

        GoalDto updatedGoal = goalService.updateStatus(id, status);
        return ResponseGenerator.success(updatedGoal, "Goal status updated successfully", request);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterGoals(@AuthenticationPrincipal User user,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String goalDescription,
            HttpServletRequest request) {

        List<GoalDto> goals = goalService.filterGoals(user.getEntityid(),status, goalDescription);
        return ResponseGenerator.success(goals, "Goals fetched successfully", request);
    }


    @GetMapping
    public ResponseEntity<?> getAllGoals(HttpServletRequest request) {
        List<GoalDto> goals = goalService.getAllGoals();
        return ResponseGenerator.success(goals, "Goals fetched successfully", request);
    }

}


