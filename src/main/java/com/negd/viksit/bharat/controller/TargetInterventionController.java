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

import com.negd.viksit.bharat.dto.TargetInterventionDto;
import com.negd.viksit.bharat.enums.PresentStatus;
import com.negd.viksit.bharat.enums.Priority;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.TargetInterventionService;
import com.negd.viksit.bharat.util.ResponseGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/five-year-plan")
public class TargetInterventionController {

	private final TargetInterventionService service;

	public TargetInterventionController(TargetInterventionService service) {
		this.service = service;
	}

	// CREATE
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody TargetInterventionDto dto, HttpServletRequest request) {
		TargetInterventionDto saved = service.save(dto);
		return ResponseGenerator.created(saved, "Target / Intervention created successfully", request);
	}

	// READ ALL
	@GetMapping("/getList")
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		List<TargetInterventionDto> list = service.findAll();
		return ResponseGenerator.success(list, "Target / Intervention fetched successfully", request);
	}

	// READ ONE
	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
		TargetInterventionDto dto = service.findById(id);
		return ResponseGenerator.success(dto, "Target / Intervention fetched successfully", request);
	}

	// UPDATE
	@PutMapping("/updateById/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TargetInterventionDto dto,
			HttpServletRequest request) {
		TargetInterventionDto updated = service.update(id, dto);
		return ResponseGenerator.success(updated, "Target / Intervention updated successfully", request);
	}

	// DELETE
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
		service.delete(id);
		return ResponseGenerator.success(null, "Target / Intervention deleted successfully", request);
	}

	// updateStatus
	@PatchMapping("/{id}/status/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @PathVariable String status,
			HttpServletRequest request) {

		TargetInterventionDto updated = service.updateStatus(id, status);
		return ResponseGenerator.success(updated, "Target / Intervention status updated successfully", request);
	}

	// FILTER
	@GetMapping("/filter")
	public ResponseEntity<?> filterTargetInterventions(@AuthenticationPrincipal User user,
			@RequestParam(required = false) String status, @RequestParam(required = false) String targetDetails,
			HttpServletRequest request) {

		List<TargetInterventionDto> list = service.filterTargetInterventions(user.getEntityid(), status, targetDetails);
		return ResponseGenerator.success(list, "Target / Intervention filtered successfully", request);
	}
	
	// priorities dropdown list
	@GetMapping("/priorities")
	public Priority[] getPriorities() {
		return Priority.values();
	}
	
	// present-status dropdown list
	@GetMapping("/present-status")
	public PresentStatus[] getPresentStatus() {
		return PresentStatus.values();
	}

}
