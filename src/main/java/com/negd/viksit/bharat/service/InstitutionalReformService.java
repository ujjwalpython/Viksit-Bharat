package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.InstitutionalReformDto;
import com.negd.viksit.bharat.dto.InstitutionalReformResponseDto;
import com.negd.viksit.bharat.dto.TargetDto;
import com.negd.viksit.bharat.model.InstitutionalReform;
import com.negd.viksit.bharat.model.Target;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.GovernmentEntityRepository;
import com.negd.viksit.bharat.repository.InstitutionalReformRepository;
import com.negd.viksit.bharat.repository.TargetRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstitutionalReformService {

	private final InstitutionalReformRepository reformRepo;
	private final TargetRepository targetRepo;
//	private final MinistryRepository ministryRepository;
	private final GovernmentEntityRepository governmentEntityRepository;
	private final DocumentRepository documentRepository;

	private static final String ID_PREFIX = "MOCVBIR";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public InstitutionalReform save(InstitutionalReform reform) {
		if (reform.getId() == null || reform.getId().isEmpty()) {
			reform.setId(generateCustomId());
		}
		// Assign milestone IDs automatically
		if (reform.getTarget() != null && !reform.getTarget().isEmpty()) {
			for (int i = 0; i < reform.getTarget().size(); i++) {
				Target milestone = reform.getTarget().get(i);
				if (milestone.getId() == null || milestone.getId().isEmpty()) {
					String milestoneId = reform.getId() + "/" + String.format("%02d", i + 1);
					milestone.setId(milestoneId);
				}
				milestone.setInstitutionalReform(reform);
			}
		}
		return reformRepo.save(reform);
	}

	private String generateCustomId() {
		String sql = "SELECT id FROM vb_core.institutional_reform WHERE id LIKE :prefix ORDER BY id DESC LIMIT 1";
//		String sql = "SELECT id FROM institutional_reform WHERE id LIKE :prefix ORDER BY id DESC LIMIT 1";
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

	// ----------------- Mapping Methods -----------------

	private Target toEntity(TargetDto dto) {
		if (dto == null)
			return null;
		Target target = Target.builder().activityDescription(dto.getActivityDescription()).deadline(dto.getDeadline())
				.build();

		if (dto.getDocumentId() != null) {
			target.setDocument(documentRepository.findById(dto.getDocumentId())
					.orElseThrow(() -> new RuntimeException("Document not found: " + dto.getDocumentId())));
		}
		return target;
	}

	private TargetDto toDto(Target t) {
		if (t == null)
			return null;
		return TargetDto.builder().id(t.getId()).activityDescription(t.getActivityDescription())
				.deadline(t.getDeadline()).documentId(t.getDocument() != null ? t.getDocument().getId() : null)
				.documentUrl(t.getDocument() != null ? t.getDocument().getFileUrl() : null).build();
	}

	private InstitutionalReform mapToEntity(InstitutionalReformDto dto) {
		InstitutionalReform reform = new InstitutionalReform();
		reform.setId(dto.getId());
//        reform.setGoalId(dto.getGoalId());

//		if (dto.getMinistryId() != null) {
//			Ministry ministry = ministryRepository.findById(dto.getMinistryId())
//					.orElseThrow(() -> new RuntimeException("Ministry not found with id: " + dto.getMinistryId()));
//			reform.setMinistry(ministry);
//		}
		if (dto.getMinistryId() != null) {
			GovernmentEntity ministry = governmentEntityRepository.findById(dto.getMinistryId()).orElseThrow(
					() -> new RuntimeException("Ministry/Department not found with id: " + dto.getMinistryId()));
			reform.setMinistry(ministry);
		}

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

	private InstitutionalReformResponseDto mapToDto(InstitutionalReform reform) {
		InstitutionalReformResponseDto dto = new InstitutionalReformResponseDto();
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
//		dto.setMinistryId(reform.getMinistry().getName());

		// Build Ministry / Department display name
//	    String ministryDisplayName = reform.getMinistry().getName();
//
//	    if (reform.getMinistry().getDepartments() != null && !reform.getMinistry().getDepartments().isEmpty()) {
//	        Department dept = reform.getMinistry().getDepartments().stream()
//	            .filter(d -> Boolean.TRUE.equals(d.getIsActive()))
//	            .findFirst()
//	            .orElse(null);
//
//	        if (dept != null) {
//	            ministryDisplayName += " / " + dept.getName();
//	        }
//	    }
//
//	    // Set the human-readable name in the ministryId field
//	    dto.setMinistryId(ministryDisplayName);
		if (reform.getMinistry() != null) {
			dto.setMinistryId(reform.getMinistry().getName());
		}

		if (reform.getTarget() != null) {
			dto.setTarget(reform.getTarget().stream().map(this::toDto).collect(Collectors.toList()));
		}
		return dto;
	}

	// ----------------- Business Methods -----------------
	// CREATE
	public InstitutionalReformResponseDto create(InstitutionalReformDto dto) {
		InstitutionalReform entity = mapToEntity(dto);
		InstitutionalReform saved = save(entity);
		return mapToDto(saved);
	}

	// READ ALL
	public List<InstitutionalReformResponseDto> getAll(com.negd.viksit.bharat.model.User user) {
		String email = user.getEmail();
		List<String> superUsers = List.of("super.admin@test.com", "cabsec.user@test.com", "pmo.user@test.com");

		List<InstitutionalReform> reforms;
		if (superUsers.contains(email)) {
			reforms = reformRepo.findAll();
		} else {
			reforms = reformRepo.findByCreatedBy(user.getEntityid());
		}

		return reforms.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// READ ONE
	public InstitutionalReformResponseDto getOne(String id) {
		return mapToDto(reformRepo.findById(id).orElseThrow());
	}

	// UPDATE
	public InstitutionalReformResponseDto update(String id, InstitutionalReformDto dto) {
		InstitutionalReform existing = reformRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Institutional Reform Not Found"));

//		Ministry ministry = ministryRepository.findById(dto.getMinistryId())
//				.orElseThrow(() -> new RuntimeException("Ministry not found"));
//
//		existing.setMinistry(ministry);
		GovernmentEntity ministry = governmentEntityRepository.findById(dto.getMinistryId())
				.orElseThrow(() -> new RuntimeException("Ministry/Department not found"));
		existing.setMinistry(ministry);
		existing.setStatus(dto.getStatus());
		existing.setInstitutionalReformName(dto.getInstitutionalReformName());
		existing.setReformDescription(dto.getReformDescription());
		existing.setReformType(dto.getReformType());
		existing.setTargetCompletionDate(dto.getTargetCompletionDate());
		existing.setRulesToBeAmended(dto.getRulesToBeAmended());
		existing.setIntendedOutcome(dto.getIntendedOutcome());
		existing.setPresentStatus(dto.getPresentStatus());
		existing.setStatus(dto.getStatus());

		if (dto.getTarget() != null) {
			var existingTargetsMap = existing.getTarget().stream().filter(t -> t.getId() != null)
					.collect(Collectors.toMap(Target::getId, t -> t));

			existing.getTarget().clear();

			for (TargetDto tDto : dto.getTarget()) {
				Target target;

				if (tDto.getId() != null && existingTargetsMap.containsKey(tDto.getId())) {
					target = existingTargetsMap.get(tDto.getId());
				} else {
					target = new Target();
				}

				target.setActivityDescription(tDto.getActivityDescription());
				target.setDeadline(tDto.getDeadline());
//				target.setDocumentPath(tDto.getDocumentPath());

				if (tDto.getDocumentId() != null) {
					target.setDocument(documentRepository.findById(tDto.getDocumentId())
							.orElseThrow(() -> new RuntimeException("Document not found: " + tDto.getDocumentId())));
				} else {
					target.setDocument(null);
				}

				existing.addTarget(target);
			}
		} else {
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

	// Update only status
	public InstitutionalReformResponseDto updateStatus(String id, String status) {
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
	public List<InstitutionalReformResponseDto> filterReforms(Long entityId, String status, String reformDescription,
			String email) {

		List<String> superUsers = List.of("super.admin@test.com", "cabsec.user@test.com", "pmo.user@test.com");
		List<InstitutionalReform> reforms;

		if (superUsers.contains(email)) {
			// Super users → see everything
			if (status == null && reformDescription == null) {
				reforms = reformRepo.findAll();
			} else if (status != null && reformDescription == null) {
				reforms = reformRepo.findByStatusIgnoreCase(status);
			} else if (status == null) {
				reforms = reformRepo.findByReformDescriptionContainingIgnoreCase(reformDescription);
			} else {
				reforms = reformRepo.findByStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(status,
						reformDescription);
			}
		} else {
			// Normal users → only their own records
			if (status == null && reformDescription == null) {
				reforms = reformRepo.findByCreatedBy(entityId);
			} else if (status != null && reformDescription == null) {
				reforms = reformRepo.findByCreatedByAndStatusIgnoreCase(entityId, status);
			} else if (status == null) {
				reforms = reformRepo.findByCreatedByAndReformDescriptionContainingIgnoreCase(entityId,
						reformDescription);
			} else {
				reforms = reformRepo.findByCreatedByAndStatusIgnoreCaseAndReformDescriptionContainingIgnoreCase(
						entityId, status, reformDescription);
			}
		}

		return reforms.stream().map(this::mapToDto).collect(Collectors.toList());
	}

}
