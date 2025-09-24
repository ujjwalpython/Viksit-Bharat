package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.GoalDto;
import com.negd.viksit.bharat.dto.GoalRespDto;
import com.negd.viksit.bharat.service.GoalService;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody GoalDto goalDto, HttpServletRequest request) {
        GoalRespDto createdGoal = goalService.createGoal(goalDto);
        return ResponseGenerator.created(createdGoal, "Goal created successfully", request);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<?> getAllGoals(HttpServletRequest request) {
        List<GoalDto> goals = goalService.getAllGoals();
        return ResponseGenerator.success(goals, "Goals fetched successfully", request);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable UUID id, HttpServletRequest request) {
        GoalDto goal = goalService.getGoalById(id);
        return ResponseGenerator.success(goal, "Goal fetched successfully", request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable UUID id, @RequestBody GoalDto goalDto, HttpServletRequest request) {
        GoalDto updatedGoal = goalService.updateGoal(id, goalDto);
        return ResponseGenerator.success(updatedGoal, "Goal updated successfully", request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable UUID id, HttpServletRequest request) {
        goalService.deleteGoal(id);
        return ResponseGenerator.success(null, "Goal deleted successfully", request);
    }
}

