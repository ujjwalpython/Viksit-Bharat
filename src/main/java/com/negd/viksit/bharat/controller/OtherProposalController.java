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

import com.negd.viksit.bharat.dto.OtherProposalDto;
import com.negd.viksit.bharat.dto.OtherProposalResponseDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.OtherProposalService;
import com.negd.viksit.bharat.util.ResponseGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/other-proposals")
public class OtherProposalController {

	private final OtherProposalService service;

	public OtherProposalController(OtherProposalService service) {
		this.service = service;
	}

	// CREATE
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody OtherProposalDto dto, HttpServletRequest request) {
		OtherProposalResponseDto saved = service.create(dto);
		return ResponseGenerator.created(saved, "Proposal created successfully", request);
	}

	// READ ALL
	@GetMapping("/getList")
	public ResponseEntity<?> getAll(HttpServletRequest request) {
		List<OtherProposalResponseDto> list = service.getAll();
		return ResponseGenerator.success(list, "Proposals fetched successfully", request);
	}

	// READ ONE
	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getById(@PathVariable String id, HttpServletRequest request) {
		OtherProposalResponseDto dto = service.getOne(id);
		return ResponseGenerator.success(dto, "Proposal fetched successfully", request);
	}

	// UPDATE
	@PutMapping("/updateById/{id}")
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody OtherProposalDto dto, HttpServletRequest request) {
		OtherProposalResponseDto updated = service.update(id, dto);
		return ResponseGenerator.success(updated, "Proposal updated successfully", request);
	}

	// DELETE
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
		service.delete(id);
		return ResponseGenerator.success(null, "Proposal deleted successfully", request);
	}

	// UPDATE STATUS
	@PatchMapping("/{id}/status/{status}")
	public ResponseEntity<?> updateStatus(@PathVariable String id, @PathVariable String status,
			HttpServletRequest request) {

		OtherProposalResponseDto updated = service.updateStatus(id, status);
		return ResponseGenerator.success(updated, "Proposal status updated successfully", request);
	}
	
	@GetMapping("/filter")
    public ResponseEntity<?> filterProposal(@AuthenticationPrincipal User user,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String proposalDescription,
            HttpServletRequest request) {

        List<OtherProposalResponseDto> list = service.filterProposals(user.getEntityid(),status, proposalDescription);
        return ResponseGenerator.success(list, "Goals fetched successfully", request);
    }

}
