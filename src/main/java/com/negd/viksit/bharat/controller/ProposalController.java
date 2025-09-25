package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.ProposalDto;
import com.negd.viksit.bharat.service.ProposalService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {

    private final ProposalService service;

    public ProposalController(ProposalService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProposalDto dto, HttpServletRequest request) {
        ProposalDto saved = service.create(dto);
        return ResponseGenerator.created(saved, "Proposal created successfully", request);
    }

    // READ ALL
    @GetMapping("/getList")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<ProposalDto> list = service.getAll();
        return ResponseGenerator.success(list, "Proposals fetched successfully", request);
    }

    // READ ONE
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        ProposalDto dto = service.getOne(id);
        return ResponseGenerator.success(dto, "Proposal fetched successfully", request);
    }

    // UPDATE
    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProposalDto dto,
                                    HttpServletRequest request) {
        ProposalDto updated = service.update(id, dto);
        return ResponseGenerator.success(updated, "Proposal updated successfully", request);
    }

    // DELETE
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id);
        return ResponseGenerator.success(null, "Proposal deleted successfully", request);
    }
}
