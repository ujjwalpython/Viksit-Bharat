package com.negd.viksit.bharat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.MilestonesListDto;
import com.negd.viksit.bharat.dto.PmInfrastructureReviewDto;
import com.negd.viksit.bharat.dto.PmInfrastructureReviewResponseDto;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.MilestonesList;
import com.negd.viksit.bharat.model.PmInfrastructureReview;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DocumentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.PmInfrastructureReviewRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class PmInfrastructureReviewService {
	private final PmInfrastructureReviewRepository repository;

	private final DocumentRepository documentRepository;

	private final MinistryRepository ministryRepository;

	public PmInfrastructureReviewService(PmInfrastructureReviewRepository repository,
			DocumentRepository documentRepository, MinistryRepository ministryRepository) {
		this.repository = repository;
		this.documentRepository = documentRepository;
		this.ministryRepository = ministryRepository;
	}

	private static final String ID_PREFIX = "MOCVBPMI";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public PmInfrastructureReview save(PmInfrastructureReview reform) {
		if (reform.getId() == null || reform.getId().isEmpty()) {
			reform.setId(generateCustomId());
		}

		// Assign milestone IDs automatically
		if (reform.getMilestonesList() != null && !reform.getMilestonesList().isEmpty()) {
			for (int i = 0; i < reform.getMilestonesList().size(); i++) {
				MilestonesList milestone = reform.getMilestonesList().get(i);
				if (milestone.getId() == null || milestone.getId().isEmpty()) {
					String milestoneId = reform.getId() + "/" + String.format("%02d", i + 1);
					milestone.setId(milestoneId);
				}
				milestone.setPmInfrastructureReview(reform);
			}
		}
		return repository.save(reform);
	}

	private String generateCustomId() {
//		String sql = "SELECT pir.id FROM vb_core.pm_infrastructure_review pir WHERE pir.id LIKE :prefix ORDER BY pir.id DESC LIMIT 1";
		String sql = "SELECT pir.id FROM pm_infrastructure_review pir WHERE pir.id LIKE :prefix ORDER BY pir.id DESC LIMIT 1";
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
	private PmInfrastructureReview convertToEntity(PmInfrastructureReviewDto dto) {
		PmInfrastructureReview entity = new PmInfrastructureReview();
		entity.setId(dto.getId());
		entity.setPrimatryMinistryDepartment(dto.getPrimatryMinistryDepartment());
		entity.setDateOfReviewMeeting(dto.getDateOfReviewMeeting());
		entity.setSupportingMinistryDepartment(dto.getSupportingMinistryDepartment());
		entity.setCategory(dto.getCategory());
		entity.setTargetCompletionDate(dto.getTargetCompletionDate());
		entity.setActionItem(dto.getActionItem());
		entity.setPriority(dto.getPriority());
		entity.setPresentStatusOfAchievement(dto.getPresentStatusOfAchievement());
		entity.setBottlenecks(dto.getBottlenecks());
		entity.setExpectedOutcome(dto.getExpectedOutcome());
		entity.setStatus(dto.getStatus());

		if (dto.getMinistryId() != null) {
			Ministry ministry = ministryRepository.findById(dto.getMinistryId())
					.orElseThrow(() -> new RuntimeException("Ministry not found with id: " + dto.getMinistryId()));
			entity.setMinistry(ministry);
		}

		if (dto.getMilestonesList() != null) {
			dto.getMilestonesList().forEach(kdDto -> {
				MilestonesList kd = new MilestonesList();
				kd.setActivityDescription(kdDto.getActivityDescription());
				kd.setDeadline(kdDto.getDeadline());
				if (kdDto.getDocumentId() != null) {
					Document doc = documentRepository.findById(kdDto.getDocumentId())
							.orElseThrow(() -> new RuntimeException("Document not found: " + kdDto.getDocumentId()));
					kd.setDocument(doc);
				}
				entity.addMilestonesList(kd);
			});
		}
		return entity;
	}

	private PmInfrastructureReviewResponseDto convertToDto(PmInfrastructureReview entity) {
		PmInfrastructureReviewResponseDto dto = new PmInfrastructureReviewResponseDto();
		dto.setId(entity.getId());

		// Build Ministry / Department display name
		String ministryDisplayName = entity.getMinistry().getName();

		if (entity.getMinistry().getDepartments() != null && !entity.getMinistry().getDepartments().isEmpty()) {
			Department dept = entity.getMinistry().getDepartments().stream()
					.filter(d -> Boolean.TRUE.equals(d.getIsActive())).findFirst().orElse(null);

			if (dept != null) {
				ministryDisplayName += " / " + dept.getName();
			}
		}

		// Set the human-readable name in the ministryId field
		dto.setMinistryId(ministryDisplayName);

		dto.setPrimatryMinistryDepartment(entity.getPrimatryMinistryDepartment());
		dto.setDateOfReviewMeeting(entity.getDateOfReviewMeeting());
		dto.setSupportingMinistryDepartment(entity.getSupportingMinistryDepartment());
		dto.setCategory(entity.getCategory());
		dto.setTargetCompletionDate(entity.getTargetCompletionDate());
		dto.setActionItem(entity.getActionItem());
		dto.setPriority(entity.getPriority());
		dto.setPresentStatusOfAchievement(entity.getPresentStatusOfAchievement());
		dto.setBottlenecks(entity.getBottlenecks());
		dto.setExpectedOutcome(entity.getExpectedOutcome());
		dto.setStatus(entity.getStatus());
		dto.setLastUpdated(entity.getUpdatedOn());

		if (entity.getMilestonesList() != null) {
			List<MilestonesListDto> kdDtos = entity.getMilestonesList().stream().map(kd -> {
				MilestonesListDto kdDto = new MilestonesListDto();
				kdDto.setActivityDescription(kd.getActivityDescription());
				kdDto.setDeadline(kd.getDeadline());
				kdDto.setDocumentId(kd.getDocument() != null ? kd.getDocument().getId() : null);
				kdDto.setDocumentUrl(kd.getDocument() != null ? kd.getDocument().getFileUrl() : null);
				kdDto.setId(kd.getId());
				return kdDto;
			}).toList();
			dto.setMilestonesList(kdDtos);
		}
		return dto;
	}

	// ----------------- Business Methods -----------------
	// Create
	public PmInfrastructureReviewResponseDto save(PmInfrastructureReviewDto dto) {
		PmInfrastructureReview entity = convertToEntity(dto);
		PmInfrastructureReview saved = save(entity);
		return convertToDto(saved);
	}

	// Get All List
	public List<PmInfrastructureReviewResponseDto> findAll(User user) {
		Sort sort = Sort.by(Sort.Direction.DESC, "updatedOn");
		List<PmInfrastructureReview> entities;

		if (isSuperUser(user.getEmail())) {
			entities = repository.findAll(sort);
		} else {
			entities = repository.findByCreatedBy(user.getEntityid(), sort);
		}

		return entities.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	// Get List By Id
	public PmInfrastructureReviewResponseDto findById(String id) {
		PmInfrastructureReview entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("PmInfrastructure Review Not Found"));
		return convertToDto(entity);
	}

	// Update By Id
	public PmInfrastructureReviewResponseDto update(String id, PmInfrastructureReviewDto dto) {
		PmInfrastructureReview existing = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("PmInfrastructure Review Not Found"));

		Ministry ministry = ministryRepository.findById(dto.getMinistryId())
				.orElseThrow(() -> new RuntimeException("Ministry not found"));

		existing.setMinistry(ministry);
		existing.setPrimatryMinistryDepartment(dto.getPrimatryMinistryDepartment());
		existing.setDateOfReviewMeeting(dto.getDateOfReviewMeeting());
		existing.setSupportingMinistryDepartment(dto.getSupportingMinistryDepartment());
		existing.setCategory(dto.getCategory());
		existing.setTargetCompletionDate(dto.getTargetCompletionDate());
		existing.setActionItem(dto.getActionItem());
		existing.setPriority(dto.getPriority());
		existing.setPresentStatusOfAchievement(dto.getPresentStatusOfAchievement());
		existing.setBottlenecks(dto.getBottlenecks());
		existing.setExpectedOutcome(dto.getExpectedOutcome());
		existing.setStatus(dto.getStatus());

		if (dto.getMilestonesList() != null) {
			Map<String, MilestonesList> existingKdsMap = existing.getMilestonesList().stream()
					.filter(kd -> kd.getId() != null).collect(Collectors.toMap(MilestonesList::getId, kd -> kd));

			List<MilestonesList> updatedMilestones = new ArrayList<>();
			int counter = existing.getMilestonesList().size() + 1;

			for (MilestonesListDto kdDto : dto.getMilestonesList()) {
				MilestonesList kd;

				if (kdDto.getId() != null && existingKdsMap.containsKey(kdDto.getId())) {
					// Update existing milestone
					kd = existingKdsMap.get(kdDto.getId());
				} else {
					// New milestone
					kd = new MilestonesList();
					String milestoneId = existing.getId() + "/" + String.format("%02d", counter++);
					kd.setId(milestoneId);
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

				// Set back-reference but avoid re-adding to the old list
				kd.setPmInfrastructureReview(existing);
				updatedMilestones.add(kd);
			}

			// Replace entire milestones list with updated one
			existing.getMilestonesList().clear();
			existing.getMilestonesList().addAll(updatedMilestones);
		} else {
			existing.getMilestonesList().clear();
		}

		PmInfrastructureReview updated = repository.save(existing);
		return convertToDto(updated);
	}

	// Delete By Id
	public void delete(String id) {
		repository.deleteById(id);
	}

	// Update Status By Id
	public PmInfrastructureReviewResponseDto updateStatus(String id, String status) {
		PmInfrastructureReview entity = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("PmInfrastructure Review Not Found"));

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

		PmInfrastructureReview saved = repository.save(entity);
		return convertToDto(saved);
	}

	// Filter
	public List<PmInfrastructureReviewResponseDto> filter(Long entityId, String status, String actionItem,
			String email) {
		Sort sort = Sort.by(Sort.Direction.DESC, "updatedOn");
		List<PmInfrastructureReview> entities;

		if (isSuperUser(email)) {
			if (status == null && actionItem == null) {
				entities = repository.findAll(sort);
			} else if (status != null && actionItem == null) {
				entities = repository.findByStatusIgnoreCase(status, sort);
			} else if (status == null) {
				entities = repository.findByActionItemContainingIgnoreCase(actionItem, sort);
			} else {
				entities = repository.findByStatusIgnoreCaseAndActionItemContainingIgnoreCase(status, actionItem, sort);
			}
		} else {
			if (status == null && actionItem == null) {
				entities = repository.findByCreatedBy(entityId, sort);
			} else if (status != null && actionItem == null) {
				entities = repository.findByCreatedByAndStatusIgnoreCase(entityId, status, sort);
			} else if (status == null) {
				entities = repository.findByCreatedByAndActionItemContainingIgnoreCase(entityId, actionItem, sort);
			} else {
				entities = repository.findByCreatedByAndStatusIgnoreCaseAndActionItemContainingIgnoreCase(entityId,
						status, actionItem, sort);
			}
		}

		return entities.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private boolean isSuperUser(String email) {
		return List.of("super.admin@test.com", "cabsec.user@test.com", "pmo.user@test.com").contains(email);
	}
}
