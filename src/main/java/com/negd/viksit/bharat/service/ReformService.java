package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.InstitutionalReformDto;
import com.negd.viksit.bharat.dto.TargetDto;
import com.negd.viksit.bharat.model.InstitutionalReform;
import com.negd.viksit.bharat.model.Target;
import com.negd.viksit.bharat.repository.InstitutionalReformRepository;
import com.negd.viksit.bharat.repository.TargetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReformService {

	private final InstitutionalReformRepository reformRepo;
	private final TargetRepository targetRepo;

	// CREATE
	public InstitutionalReformDto create(InstitutionalReformDto dto) {
	    InstitutionalReform entity = mapToEntity(dto);   // ✅ use mapToEntity
	    InstitutionalReform saved = reformRepo.save(entity);
	    return mapToDto(saved);                          // ✅ use mapToDto
	}

	// READ ALL
	public List<InstitutionalReformDto> getAll() {
	    return reformRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList()); // ✅
	}

	// READ ONE
	public InstitutionalReformDto getOne(Long id) {
	    return mapToDto(reformRepo.findById(id).orElseThrow()); // ✅
	}

	// UPDATE
	public InstitutionalReformDto update(Long id, InstitutionalReformDto dto) {
	    InstitutionalReform existing = reformRepo.findById(id).orElseThrow();
	    existing.setName(dto.getName());
	    existing.setDescription(dto.getDescription());
	    existing.setReformType(dto.getReformType());
	    existing.setTargetCompletionDate(dto.getTargetCompletionDate());
	    existing.setRulesToBeAmended(dto.getRulesToBeAmended());
	    existing.setIntendedOutcome(dto.getIntendedOutcome());
	    existing.setPresentStatus(dto.getPresentStatus());
	    if (dto.getTargetId() != null) {
	        Target target = targetRepo.findById(dto.getTargetId()).orElse(null);
	        existing.setTarget(target);
	    }
	    InstitutionalReform updated = reformRepo.save(existing);
	    return mapToDto(updated); // ✅
	}

	// DELETE
	public void delete(Long id) {
		reformRepo.deleteById(id);
	}

	// TARGET LIST
	public List<TargetDto> listTargets() {
		return targetRepo.findAll().stream()
				.map(t -> TargetDto.builder().id(t.getId()).name(t.getName()).description(t.getDescription()).build())
				.collect(Collectors.toList());
	}

	private Target toEntity(TargetDto dto) {
	    if (dto == null) return null;
	    Target target = new Target();
	    target.setId(dto.getId());
	    target.setName(dto.getName());
	    target.setDescription(dto.getDescription());
	    return target;
	}
	
	private TargetDto toDto(Target t) {
	    if (t == null) return null;
	    TargetDto dto = new TargetDto();
	    dto.setId(t.getId());
	    dto.setName(t.getName());
	    dto.setDescription(t.getDescription());
	    return dto;
	}
	
	private InstitutionalReform mapToEntity(InstitutionalReformDto dto) {
	    InstitutionalReform reform = new InstitutionalReform();
	    reform.setId(dto.getId());
	    reform.setName(dto.getName());
	    reform.setDescription(dto.getDescription());
	    reform.setReformType(dto.getReformType());
	    reform.setTargetCompletionDate(dto.getTargetCompletionDate());
	    reform.setRulesToBeAmended(dto.getRulesToBeAmended());
	    reform.setIntendedOutcome(dto.getIntendedOutcome());
	    reform.setPresentStatus(dto.getPresentStatus());

	    if (dto.getTargetId() != null) {
	        reform.setTarget(targetRepo.findById(dto.getTargetId()).orElse(null));
	    }
	    return reform;
	}

	private InstitutionalReformDto mapToDto(InstitutionalReform reform) {
	    InstitutionalReformDto dto = new InstitutionalReformDto();
	    dto.setId(reform.getId());
	    dto.setName(reform.getName());
	    dto.setDescription(reform.getDescription());
	    dto.setReformType(reform.getReformType());
	    dto.setTargetCompletionDate(reform.getTargetCompletionDate());
	    dto.setRulesToBeAmended(reform.getRulesToBeAmended());
	    dto.setIntendedOutcome(reform.getIntendedOutcome());
	    dto.setPresentStatus(reform.getPresentStatus());

	    if (reform.getTarget() != null) {
	        dto.setTargetId(reform.getTarget().getId());
	        dto.setTarget(toDto(reform.getTarget())); // ✅ TargetDto me convert
	    }
	    return dto;
	}

}
