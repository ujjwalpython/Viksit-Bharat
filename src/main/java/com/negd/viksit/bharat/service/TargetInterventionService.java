package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.KeyDeliverableDto;
import com.negd.viksit.bharat.dto.TargetInterventionDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.KeyDeliverable;
import com.negd.viksit.bharat.model.TargetIntervention;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.TargetInterventionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class TargetInterventionService {

	private final TargetInterventionRepository repository;

	private final DocumentRepository documentRepository;

	public TargetInterventionService(TargetInterventionRepository repository, DocumentRepository documentRepository) {
		this.repository = repository;
		this.documentRepository = documentRepository;
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
		// Fixed table name to target_interventions for ID generation
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
		entity.setMinistry(dto.getMinistry());
		entity.setGoalId(dto.getGoalId());
		entity.setTargetDetails(dto.getTargetDetails());
		entity.setActionPoint(dto.getActionPoint());
		entity.setTargetDate(dto.getTargetDate());

		entity.setPriority(dto.getPriority());
		entity.setPresentStatus(dto.getPresentStatus());
		// entity.setImplementationStatus(dto.getImplementationStatus());
		entity.setBottlenecks(dto.getBottlenecks());
		entity.setStatus(dto.getStatus());

		if (dto.getKeyDeliverables() != null) {
			dto.getKeyDeliverables().forEach(kdDto -> {
				KeyDeliverable kd = new KeyDeliverable();
				kd.setActivityDescription(kdDto.getActivityDescription());
				kd.setDeadline(kdDto.getDeadline());
//                kd.setProgressMade(kdDto.getProgressMade());
				kd.setDocumentPath(kdDto.getDocumentPath());
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

	private TargetInterventionDto convertToDto(TargetIntervention entity) {
		TargetInterventionDto dto = new TargetInterventionDto();
		dto.setId(entity.getId());
		dto.setGoalId(entity.getGoalId());
		dto.setTargetDetails(entity.getTargetDetails());
		dto.setActionPoint(entity.getActionPoint());
		dto.setTargetDate(entity.getTargetDate());

		dto.setPriority(entity.getPriority());
		dto.setPresentStatus(entity.getPresentStatus());
		// dto.setImplementationStatus(entity.getImplementationStatus()); // commented
		// out in your code
		dto.setBottlenecks(entity.getBottlenecks());
		dto.setStatus(entity.getStatus());
		dto.setLastUpdated(entity.getUpdatedOn());

		dto.setMinistry(entity.getCreatedBy() != null ? entity.getCreatedBy().toString() : "N/A");

		if (entity.getKeyDeliverables() != null) {
			List<KeyDeliverableDto> kdDtos = entity.getKeyDeliverables().stream().map(kd -> {
				KeyDeliverableDto kdDto = new KeyDeliverableDto();
				kdDto.setActivityDescription(kd.getActivityDescription());
				kdDto.setDeadline(kd.getDeadline());
//                kdDto.setProgressMade(kd.getProgressMade());
				kdDto.setDocumentPath(kd.getDocumentPath());
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

	public TargetInterventionDto save(TargetInterventionDto dto) {
		TargetIntervention entity = convertToEntity(dto);
		TargetIntervention saved = save(entity); // Use the save with ID generation
		return convertToDto(saved);
	}

	public List<TargetInterventionDto> findAll() {
		return repository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public TargetInterventionDto findById(String id) {
		TargetIntervention entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));
		return convertToDto(entity);
	}

//	public TargetInterventionDto update(String id, TargetInterventionDto dto) {
//	    TargetIntervention existing = repository.findById(id)
//	            .orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));
//	    
//	    // Update main TargetIntervention fields
//	    existing.setGoalId(dto.getGoalId());
//	    existing.setMinistry(dto.getMinistry());
//	    existing.setTargetDetails(dto.getTargetDetails());
//	    existing.setActionPoint(dto.getActionPoint());
//	    existing.setTargetDate(dto.getTargetDate());
//
//	    existing.setPresentStatus(dto.getPresentStatus());
//	    // existing.setImplementationStatus(dto.getImplementationStatus()); // Uncomment if you use it
//	    existing.setPriority(dto.getPriority());
//	    existing.setBottlenecks(dto.getBottlenecks());
//
//	    // Handle KeyDeliverables update and addition
//	    if (dto.getKeyDeliverables() != null) {
//	        // Map existing KeyDeliverables by their Long ID
//	        Map<Long, KeyDeliverable> existingKdsMap = existing.getKeyDeliverables().stream()
//	                .filter(kd -> kd.getId() != null)
//	                .collect(Collectors.toMap(KeyDeliverable::getId, kd -> kd));
//
//	        // Clear current list to replace with updated/new deliverables
//	        existing.getKeyDeliverables().clear();
//
//	        for (KeyDeliverableDto kdDto : dto.getKeyDeliverables()) {
//	            KeyDeliverable kd;
//
//	            if (kdDto.getId() != null && existingKdsMap.containsKey(kdDto.getId())) {
//	                // Update existing KeyDeliverable
//	                kd = existingKdsMap.get(kdDto.getId());
//	            } else {
//	                // Create new KeyDeliverable if no ID present or not found
//	                kd = new KeyDeliverable();
//	            }
//
//	            kd.setActivityDescription(kdDto.getActivityDescription());
//	            kd.setDeadline(kdDto.getDeadline());
//	            // kd.setProgressMade(kdDto.getProgressMade()); // Uncomment if you track progress
//	            kd.setDocumentPath(kdDto.getDocumentPath());
//
//	            existing.addKeyDeliverable(kd);
//	        }
//	    } else {
//	        // If null in DTO, clear all existing KeyDeliverables
//	        existing.getKeyDeliverables().clear();
//	    }
//
//	    // Save and return updated entity as DTO
//	    TargetIntervention updated = repository.save(existing);
//	    return convertToDto(updated);
//	}
	public TargetInterventionDto update(String id, TargetInterventionDto dto) {
		TargetIntervention existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Target / Intervention Not Found"));

		// 🔹 Update main TargetIntervention fields
		existing.setGoalId(dto.getGoalId());
		existing.setMinistry(dto.getMinistry());
		existing.setTargetDetails(dto.getTargetDetails());
		existing.setActionPoint(dto.getActionPoint());
		existing.setTargetDate(dto.getTargetDate());
		existing.setPresentStatus(dto.getPresentStatus());
		existing.setPriority(dto.getPriority());
		existing.setBottlenecks(dto.getBottlenecks());

		// 🔹 Handle KeyDeliverables update and addition
		if (dto.getKeyDeliverables() != null) {
			// Map existing KeyDeliverables by their ID
			Map<Long, KeyDeliverable> existingKdsMap = existing.getKeyDeliverables().stream()
					.filter(kd -> kd.getId() != null).collect(Collectors.toMap(KeyDeliverable::getId, kd -> kd));

			// Clear current list so we can re-add updated/new deliverables
			existing.getKeyDeliverables().clear();

			for (KeyDeliverableDto kdDto : dto.getKeyDeliverables()) {
				KeyDeliverable kd;

				if (kdDto.getId() != null && existingKdsMap.containsKey(kdDto.getId())) {
					// 🔹 Existing KeyDeliverable → Update
					kd = existingKdsMap.get(kdDto.getId());
				} else {
					// 🔹 New KeyDeliverable → Create
					kd = new KeyDeliverable();
				}

				kd.setActivityDescription(kdDto.getActivityDescription());
				kd.setDeadline(kdDto.getDeadline());

				// 🔗 Document linking
				if (kdDto.getDocumentId() != null) {
					Document doc = documentRepository.findById(kdDto.getDocumentId())
							.orElseThrow(() -> new RuntimeException("Document not found: " + kdDto.getDocumentId()));
					kd.setDocument(doc);
				} else {
					kd.setDocument(null); // अगर frontend null भेजे तो unlink कर देंगे
				}

				existing.addKeyDeliverable(kd);
			}
		} else {
			// अगर frontend null भेजे → सभी deliverables हटा देंगे
			existing.getKeyDeliverables().clear();
		}

		// 🔹 Save and return updated entity as DTO
		TargetIntervention updated = repository.save(existing);
		return convertToDto(updated);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}

	public TargetInterventionDto updateStatus(String id, String status) {
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

	public List<TargetInterventionDto> filterTargetInterventions(Long entityId, String status, String targetDetails) {
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
