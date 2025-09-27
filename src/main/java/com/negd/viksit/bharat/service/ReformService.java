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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReformService {

	private final InstitutionalReformRepository reformRepo;
	private final TargetRepository targetRepo;

	private static final String ID_PREFIX = "MOCVBIR";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public InstitutionalReform save(InstitutionalReform reform) {
		if (reform.getId() == null || reform.getId().isEmpty()) {
			reform.setId(generateCustomId());
		}
		return reformRepo.save(reform);
	}

	private String generateCustomId() {
		// Native query to find last ID starting with prefix ordered descending
		String sql = "SELECT id FROM vb_core.institutional_reform WHERE id LIKE :prefix ORDER BY id DESC LIMIT 1";
//    	String sql = "SELECT id FROM institutional_reform WHERE id LIKE :prefix ORDER BY id DESC LIMIT 1";
		List<String> result = entityManager.createNativeQuery(sql).setParameter("prefix", ID_PREFIX + "%")
				.getResultList();

		int nextNumber = 1;
		if (!result.isEmpty()) {
			String lastId = result.get(0);
			String numberPart = lastId.substring(ID_PREFIX.length());
			try {
				nextNumber = Integer.parseInt(numberPart) + 1;
			} catch (NumberFormatException e) {
				nextNumber = 1;
			}
		}
		return ID_PREFIX + String.format("%02d", nextNumber);
	}

	// CREATE
	public InstitutionalReformDto create(InstitutionalReformDto dto) {
		InstitutionalReform entity = mapToEntity(dto);
		InstitutionalReform saved = save(entity);
		return mapToDto(saved);
	}

	// READ ALL
	public List<InstitutionalReformDto> getAll() {
		return reformRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// READ ONE
	public InstitutionalReformDto getOne(String id) {
		return mapToDto(reformRepo.findById(id).orElseThrow());
	}

	// UPDATE
	public InstitutionalReformDto update(String id, InstitutionalReformDto dto) {
		InstitutionalReform existing = reformRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Institutional Reform Not Found"));

		// Update main fields
		existing.setMinistry(dto.getMinistry());
		existing.setInstitutionalReformName(dto.getInstitutionalReformName());
		existing.setReformDescription(dto.getReformDescription());
		existing.setReformType(dto.getReformType());
		existing.setTargetCompletionDate(dto.getTargetCompletionDate());
		existing.setRulesToBeAmended(dto.getRulesToBeAmended());
		existing.setIntendedOutcome(dto.getIntendedOutcome());
		existing.setPresentStatus(dto.getPresentStatus());
		existing.setStatus(dto.getStatus());

		// Handle Targets update and addition
		if (dto.getTarget() != null) {
			// Map existing Targets by their ID
			var existingTargetsMap = existing.getTarget().stream().filter(t -> t.getId() != null)
					.collect(Collectors.toMap(Target::getId, t -> t));

			// Clear current list to replace with updated/new targets
			existing.getTarget().clear();

			for (TargetDto tDto : dto.getTarget()) {
				Target target;

				if (tDto.getId() != null && existingTargetsMap.containsKey(tDto.getId())) {
					// Update existing Target
					target = existingTargetsMap.get(tDto.getId());
				} else {
					// Create new Target if no ID present or not found
					target = new Target();
				}

				target.setActivityDescription(tDto.getActivityDescription());
				target.setDeadline(tDto.getDeadline());
				target.setDocumentPath(tDto.getDocumentPath());

				existing.addTarget(target);
			}
		} else {
			// If null in DTO, clear all existing Targets
			existing.getTarget().clear();
		}

		InstitutionalReform updated = reformRepo.save(existing);
		return mapToDto(updated);
	}

	// DELETE
	public void delete(String id) {
		reformRepo.deleteById(id);
	}

	// TARGET LIST
	public List<TargetDto> listTargets() {
		return targetRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	// ----------------- Mapping Methods -----------------

	private Target toEntity(TargetDto dto) {
		if (dto == null)
			return null;
		return Target.builder().activityDescription(dto.getActivityDescription()).deadline(dto.getDeadline())
				.documentPath(dto.getDocumentPath()).build();
	}

	private TargetDto toDto(Target t) {
		if (t == null)
			return null;
		return TargetDto.builder().id(t.getId()).activityDescription(t.getActivityDescription())
				.deadline(t.getDeadline()).documentPath(t.getDocumentPath()).build();
	}

	private InstitutionalReform mapToEntity(InstitutionalReformDto dto) {
		InstitutionalReform reform = new InstitutionalReform();
		reform.setId(dto.getId());
//        reform.setGoalId(dto.getGoalId());
		reform.setMinistry(dto.getMinistry());
		reform.setInstitutionalReformName(dto.getInstitutionalReformName());
		reform.setReformDescription(dto.getReformDescription());
		reform.setReformType(dto.getReformType());
		reform.setTargetCompletionDate(dto.getTargetCompletionDate());
		reform.setRulesToBeAmended(dto.getRulesToBeAmended());
		reform.setIntendedOutcome(dto.getIntendedOutcome());
		reform.setPresentStatus(dto.getPresentStatus());
		reform.setStatus(dto.getStatus());

		if (dto.getTarget() != null) {
			dto.getTarget().forEach(tDto -> {
				Target trgt = toEntity(tDto);
				reform.addTarget(trgt);
			});
		}
		return reform;
	}

	private InstitutionalReformDto mapToDto(InstitutionalReform reform) {
		InstitutionalReformDto dto = new InstitutionalReformDto();
		dto.setId(reform.getId());
//        dto.setGoalId(reform.getGoalId()); 
		dto.setInstitutionalReformName(reform.getInstitutionalReformName());
		dto.setReformDescription(reform.getReformDescription());
		dto.setReformType(reform.getReformType());
		dto.setTargetCompletionDate(reform.getTargetCompletionDate());
		dto.setRulesToBeAmended(reform.getRulesToBeAmended());
		dto.setIntendedOutcome(reform.getIntendedOutcome());
		dto.setPresentStatus(reform.getPresentStatus());
		dto.setStatus(reform.getStatus());
		dto.setLastUpdated(reform.getUpdatedOn());
		dto.setMinistry(reform.getCreatedBy() != null ? reform.getCreatedBy().toString() : "N/A");

		if (reform.getTarget() != null) {
			dto.setTarget(reform.getTarget().stream().map(this::toDto).collect(Collectors.toList()));
		}
		return dto;
	}

	// Update only status
	public InstitutionalReformDto updateStatus(String id, String status) {
		InstitutionalReform reform = reformRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Institutional Reform not found with id: " + id));

		String newStatus = status.toUpperCase();
		switch (newStatus) {
		case "DRAFT":
		case "SUBMITTED":
		case "APPROVED":
		case "REJECTED":
			reform.setStatus(newStatus);
			break;
		default:
			throw new IllegalArgumentException("Invalid status: " + newStatus);
		}

		InstitutionalReform saved = reformRepo.save(reform);
		return mapToDto(saved);
	}

	// Filters
	public List<InstitutionalReformDto> filterReforms(Long entityid, String status, String reformDescription) {
		List<InstitutionalReform> reforms;

		if (status == null && reformDescription == null) {
			reforms = reformRepo.findByCreatedBy(entityid);
		} else if (status != null && reformDescription == null) {
			reforms = reformRepo.findByCreatedByAndStatusIgnoreCase(entityid, status);
		} else if (status == null) {
			reforms = reformRepo.findByCreatedByAndReformDescriptionContainingIgnoreCase(entityid, reformDescription);
		} else {
			reforms = reformRepo.findByCreatedByAndStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(entityid,
					status, reformDescription);
		}

		return reforms.stream().map(this::mapToDto).collect(Collectors.toList());
	}

}
