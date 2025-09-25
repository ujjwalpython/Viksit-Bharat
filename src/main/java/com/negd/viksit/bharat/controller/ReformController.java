package com.negd.viksit.bharat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.negd.viksit.bharat.dto.InstitutionalReformDto;
import com.negd.viksit.bharat.dto.TargetDto;
import com.negd.viksit.bharat.service.ReformService;
import com.negd.viksit.bharat.util.ResponseGenerator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/reforms")
public class ReformController {

    private final ReformService service;

    public ReformController(ReformService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody InstitutionalReformDto dto, HttpServletRequest request) {
        InstitutionalReformDto saved = service.create(dto);
        return ResponseGenerator.created(saved, "Institutional Reform created successfully", request);
    }

    // READ ALL
    @GetMapping("/getList")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        List<InstitutionalReformDto> list = service.getAll();
        return ResponseGenerator.success(list, "Institutional Reforms fetched successfully", request);
    }

    // READ ONE
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, HttpServletRequest request) {
        InstitutionalReformDto dto = service.getOne(id);
        return ResponseGenerator.success(dto, "Institutional Reform fetched successfully", request);
    }

    // UPDATE
    @PutMapping("/updateById/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody InstitutionalReformDto dto,
                                    HttpServletRequest request) {
        InstitutionalReformDto updated = service.update(id, dto);
        return ResponseGenerator.success(updated, "Institutional Reform updated successfully", request);
    }

    // DELETE
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id);
        return ResponseGenerator.success(null, "Institutional Reform deleted successfully", request);
    }

    // GET TARGET LIST
    @GetMapping("/targets")
    public ResponseEntity<?> listTargets(HttpServletRequest request) {
        List<TargetDto> targets = service.listTargets();
        return ResponseGenerator.success(targets, "Targets fetched successfully", request);
    }
}
