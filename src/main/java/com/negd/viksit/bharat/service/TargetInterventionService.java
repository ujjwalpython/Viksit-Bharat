package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.KeyDeliverableDto;
import com.negd.viksit.bharat.dto.TargetInterventionDto;
import com.negd.viksit.bharat.dto.TargetInterventionResponseDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.KeyDeliverable;
import com.negd.viksit.bharat.model.TargetIntervention;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.TargetInterventionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TargetInterventionService {

	private final TargetInterventionRepository repository;

	private final DocumentRepository documentRepository;

	private final MinistryRepository ministryRepository;

	public TargetInterventionService(TargetInterventionRepository repository, DocumentRepository documentRepository,
			MinistryRepository ministryRepository) {
		this.repository = repository;
		this.documentRepository = documentRepository;
		this.ministryRepository = ministryRepository;
	}

	private static final String ID_PREFIX = "MOCVBGA";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public TargetIntervention save(TargetIntervention reform) {
		if (reform.getId() == null || reform.getId().isEmpty()) {
			reform.setId(generateCustomId());
		}
		return repository.save(reform);
	}

	private String generateCustomId() {
		String sql = "SELECT ti.id FROM vb_core.target_interventions ti WHERE ti.id LIKE :prefix ORDER BY ti.id DESC LIMIT 1";
//    	String sql = "SELECT ti.id FROM target_interventions ti WHERE ti.id LIKE :prefix ORDER BY ti.id DESC LIMIT 1";
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

	// ----------------- Mapper Methods -----------------
	private TargetIntervention convertToEntity(TargetInterventionDto dto) {
		TargetIntervention entity = new TargetIntervention();
		entity.setId(dto.getId());
//		entity.setGoalId(dto.getGoalId());
		entity.setTargetDetails(dto.getTargetDetails());
		entity.setActionPoint(dto.getActionPoint());
		entity.setTargetDate(dto.getTargetDate());
		entity.setPriority(dto.getPriority());
		entity.setPresentStatus(dto.getPresentStatus());
		// entity.setImplementationStatus(dto.getImplementationStatus());
		entity.setBottlenecks(dto.getBottlenecks());
		entity.setStatus(dto.getStatus());

		if (dto.getMinistryId() != null) {
			Ministry ministry = ministryRepository.findById(dto.getMinistryId())
					.orElseThrow(() -> new RuntimeException("Ministry not found with id: " + dto.getMinistryId()));
			entity.setMinistry(ministry);
		}

		if (dto.getKeyDeliverables() != null) {
			dto.getKeyDeliverables().forEach(kdDto -> {
				KeyDeliverable kd = new KeyDeliverable();
				kd.setActivityDescription(kdDto.getActivityDescription());
				kd.setDeadline(kdDto.getDeadline());
//                kd.setProgressMade(kdDto.getProgressMade());
//				kd.setDocumentPath(kdDto.getDocumentPath());
				if (kdDto.getDocumentId() != null) {
					Document doc = documentRepository.findById(kdDto.getDocumentId())
							.orElseThrow(() -> new RuntimeException("Document not found: " + kdDto.getDocumentId()));
					kd.setDocument(doc);
				}
				entity.addKeyDeliverable(kd);
			});
		}
		return entity;
	}

	private TargetInterventionResponseDto convertToDto(TargetIntervention entity) {
		TargetInterventionResponseDto dto = new TargetInterventionResponseDto();
		dto.setId(entity.getId());
//		dto.setGoalId(entity.getGoalId());
		dto.setMinistryId(entity.getMinistry().getName());
		dto.setTargetDetails(entity.getTargetDetails());
		dto.setActionPoint(entity.getActionPoint());
		dto.setTargetDate(entity.getTargetDate());

		dto.setPriority(entity.getPriority());
		dto.setPresentStatus(entity.getPresentStatus());
		// dto.setImplementationStatus(entity.getImplementationStatus());
		dto.setBottlenecks(entity.getBottlenecks());
		dto.setStatus(entity.getStatus());
		dto.setLastUpdated(entity.getUpdatedOn());

		if (entity.getKeyDeliverables() != null) {
			List<KeyDeliverableDto> kdDtos = entity.getKeyDeliverables().stream().map(kd -> {
				KeyDeliverableDto kdDto = new KeyDeliverableDto();
				kdDto.setActivityDescription(kd.getActivityDescription());
				kdDto.setDeadline(kd.getDeadline());
//                kdDto.setProgressMade(kd.getProgressMade());
//				kdDto.setDocumentPath(kd.getDocumentPath());
				kdDto.setDocumentId(kd.getDocument() != null ? kd.getDocument().getId() : null);
				kdDto.setDocumentUrl(kd.getDocument() != null ? kd.getDocument().getFileUrl() : null);
				kdDto.setId(kd.getId());
				return kdDto;
			}).toList();
			dto.setKeyDeliverables(kdDtos);
		}
		return dto;
	}

	// ----------------- Business Methods -----------------
    // Create
	public TargetInterventionResponseDto save(TargetInterventionDto dto) {
		TargetIntervention entity = convertToEntity(dto);
		TargetIntervention saved = save(entity); // Use the save with ID generation
		return convertToDto(saved);
	}

	// Get All List
	public List<TargetInterventionResponseDto> findAll() {
		return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	// Get List By Id
	public TargetInterventionResponseDto findById(String id) {
		TargetIntervention entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));
		return convertToDto(entity);
	}

    // Update By Id
	public TargetInterventionResponseDto update(String id, TargetInterventionDto dto) {
		TargetIntervention existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));

		Ministry ministry = ministryRepository.findById(dto.getMinistryId())
				.orElseThrow(() -> new RuntimeException("Ministry not found"));

		existing.setMinistry(ministry);
		existing.setStatus(dto.getStatus());
//		existing.setGoalId(dto.getGoalId());
		existing.setTargetDetails(dto.getTargetDetails());
		existing.setActionPoint(dto.getActionPoint());
		existing.setTargetDate(dto.getTargetDate());
		existing.setPresentStatus(dto.getPresentStatus());
		existing.setPriority(dto.getPriority());
		existing.setBottlenecks(dto.getBottlenecks());

		if (dto.getKeyDeliverables() != null) {
			Map<Long, KeyDeliverable> existingKdsMap = existing.getKeyDeliverables().stream()
					.filter(kd -> kd.getId() != null).collect(Collectors.toMap(KeyDeliverable::getId, kd -> kd));

			existing.getKeyDeliverables().clear();

			for (KeyDeliverableDto kdDto : dto.getKeyDeliverables()) {
				KeyDeliverable kd;

				if (kdDto.getId() != null && existingKdsMap.containsKey(kdDto.getId())) {
					kd = existingKdsMap.get(kdDto.getId());
				} else {
					kd = new KeyDeliverable();
				}

				kd.setActivityDescription(kdDto.getActivityDescription());
				kd.setDeadline(kdDto.getDeadline());

				if (kdDto.getDocumentId() != null) {
					Document doc = documentRepository.findById(kdDto.getDocumentId())
							.orElseThrow(() -> new RuntimeException("Document not found: " + kdDto.getDocumentId()));
					kd.setDocument(doc);
				} else {
					kd.setDocument(null);
				}

				existing.addKeyDeliverable(kd);
			}
		} else {
			existing.getKeyDeliverables().clear();
		}

		TargetIntervention updated = repository.save(existing);
		return convertToDto(updated);
	}
 
	// Delete By Id
	public void delete(String id) {
		repository.deleteById(id);
	}

	// Update Status By Id
	public TargetInterventionResponseDto updateStatus(String id, String status) {
		TargetIntervention entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));

		String newStatus = status.toUpperCase();
		switch (newStatus) {
		case "DRAFT":
		case "SUBMITTED":
		case "APPROVED":
		case "REJECTED":
			entity.setStatus(newStatus);
			break;
		default:
			throw new IllegalArgumentException("Invalid status: " + newStatus);
		}

		TargetIntervention saved = repository.save(entity);
		return convertToDto(saved);
	}

	// Filter
	public List<TargetInterventionResponseDto> filterTargetInterventions(Long entityId, String status,
			String targetDetails) {
		List<TargetIntervention> entities;

		if (status == null && targetDetails == null) {
			entities = repository.findByCreatedBy(entityId);
		} else if (status != null && targetDetails == null) {
			entities = repository.findByCreatedByAndStatusIgnoreCase(entityId, status);
		} else if (status == null) {
			entities = repository.findByCreatedByAndTargetDetailsContainingIgnoreCase(entityId, targetDetails);
		} else {
			entities = repository.findByCreatedByAndStatusIgnoreCaseAndTargetDetailsContainingIgnoreCase(entityId,
					status, targetDetails);
		}

		return entities.stream().map(this::convertToDto).collect(Collectors.toList());
	}

}
