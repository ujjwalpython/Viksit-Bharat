package com.negd.viksit.bharat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.negd.viksit.bharat.dto.PmInfrastructureReviewDto;
import com.negd.viksit.bharat.dto.PmInfrastructureReviewResponseDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.PmInfrastructureReviewService;
import com.negd.viksit.bharat.util.ResponseGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/pm-infrastructure-review")
public class PmInfrastructureReviewController {
	private final PmInfrastructureReviewService service;

	public PmInfrastructureReviewController(PmInfrastructureReviewService service) {
		this.service = service;
	}

	// CREATE
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody PmInfrastructureReviewDto dto, HttpServletRequest request) {
		PmInfrastructureReviewResponseDto saved = service.save(dto);
		return ResponseGenerator.created(saved, "PmInfrastructure Review created successfully", request);
	}

	// READ ALL
	@GetMapping("/getList")
	public ResponseEntity<?> getAll(@AuthenticationPrincipal User user, HttpServletRequest request) {
	    List<PmInfrastructureReviewResponseDto> list = service.findAll(user);
	    return ResponseGenerator.success(list, "Data fetched successfully", request);
	}

	// READ ONE
	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable String id, HttpServletRequest request) {
		PmInfrastructureReviewResponseDto dto = service.findById(id);
		return ResponseGenerator.success(dto, "PmInfrastructure Review fetched successfully", request);
	}

	// UPDATE
	@PutMapping("/updateById/{id}")
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody PmInfrastructureReviewDto dto,
			HttpServletRequest request) {
		PmInfrastructureReviewResponseDto updated = service.update(id, dto);
		return ResponseGenerator.success(updated, "PmInfrastructure Review updated successfully", request);
	}

	// DELETE
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
		service.delete(id);
		return ResponseGenerator.success(null, "PmInfrastructure Review deleted successfully", request);
	}

	// updateStatus
	@PatchMapping("/{id}/status/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @PathVariable String status,
			HttpServletRequest request) {

		PmInfrastructureReviewResponseDto updated = service.updateStatus(id, status);
		return ResponseGenerator.success(updated, "PmInfrastructure Review status updated successfully", request);
	}

	// FILTER
	@GetMapping("/filter")
	public ResponseEntity<?> filter(
	        @AuthenticationPrincipal User user,
	        @RequestParam(required = false) String status,
	        @RequestParam(required = false) String actionItem,
	        HttpServletRequest request) {
	    List<PmInfrastructureReviewResponseDto> list =
	        service.filter(user.getEntityid(), status, actionItem, user.getEmail());
	    return ResponseGenerator.success(list, "Filtered data fetched successfully", request);
	}

}
