package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.OtherProposalDto;
import com.negd.viksit.bharat.dto.OtherProposalResponseDto;
import com.negd.viksit.bharat.model.OtherProposal;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.ProposalRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class OtherProposalService {

	private final ProposalRepository repository;

	private final MinistryRepository ministryRepository;

	public OtherProposalService(ProposalRepository repository, MinistryRepository ministryRepository) {
		this.repository = repository;
		this.ministryRepository = ministryRepository;
	}

	private static final String ID_PREFIX = "MOCVBIP";

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public OtherProposal save(OtherProposal reform) {
		if (reform.getId() == null || reform.getId().isEmpty()) {
			reform.setId(generateCustomId());
		}
		return repository.save(reform);
	}

	private String generateCustomId() {
//		String sql = "SELECT ir.id FROM vb_core.other_proposals ir WHERE ir.id LIKE :prefix ORDER BY ir.id DESC LIMIT 1";
		String sql = "SELECT ir.id FROM other_proposals ir WHERE ir.id LIKE :prefix ORDER BY ir.id DESC LIMIT 1";
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
	private OtherProposalResponseDto mapToDto(OtherProposal entity) {
		return OtherProposalResponseDto.builder().id(entity.getId()).ideaProposalTitle(entity.getIdeaProposalTitle())
				.proposalDescription(entity.getProposalDescription()).proposalType(entity.getProposalType())
				.potentialEconomicDevelopment(entity.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(entity.getPotentialEmploymentGeneration())
				.timelineStart(entity.getTimelineStart()).timelineEnd(entity.getTimelineEnd())
				.status(entity.getStatus()).lastUpdated(entity.getUpdatedOn())
				.ministryId(entity.getMinistry().getName()).build();
	}

	private OtherProposal mapToEntity(OtherProposalDto dto) {
		Ministry ministry = null;
		if (dto.getMinistryId() != null) {
			ministry = ministryRepository.findById(dto.getMinistryId())
					.orElseThrow(() -> new RuntimeException("Ministry not found with id: " + dto.getMinistryId()));
		}
		return OtherProposal.builder().id(dto.getId()).ministry(ministry).ideaProposalTitle(dto.getIdeaProposalTitle())
				.proposalDescription(dto.getProposalDescription()).proposalType(dto.getProposalType())
				.potentialEconomicDevelopment(dto.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(dto.getPotentialEmploymentGeneration())
				.timelineStart(dto.getTimelineStart()).timelineEnd(dto.getTimelineEnd()).status(dto.getStatus())
				.build();
	}

	// ----------------- Business Methods -----------------
	// Create
	public OtherProposalResponseDto create(OtherProposalDto dto) {
		OtherProposal entity = mapToEntity(dto);
		OtherProposal saved = save(entity);
		return mapToDto(saved);
	}

	// GetAllList
	public List<OtherProposalResponseDto> getAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// GetListById
	public OtherProposalResponseDto getOne(String id) {
		return repository.findById(id).map(this::mapToDto)
				.orElseThrow(() -> new RuntimeException("Proposal not found"));
	}

	// UpdateById
	public OtherProposalResponseDto update(String id, OtherProposalDto dto) {
		OtherProposal existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Proposal not found"));
		existing.setIdeaProposalTitle(dto.getIdeaProposalTitle());
//		existing.setGoalId(dto.getGoalId());

		Ministry ministry = ministryRepository.findById(dto.getMinistryId())
				.orElseThrow(() -> new RuntimeException("Ministry not found"));

		existing.setMinistry(ministry);

		existing.setProposalDescription(dto.getProposalDescription());
		existing.setProposalType(dto.getProposalType());
		existing.setPotentialEconomicDevelopment(dto.getPotentialEconomicDevelopment());
		existing.setPotentialEmploymentGeneration(dto.getPotentialEmploymentGeneration());
		existing.setTimelineStart(dto.getTimelineStart());
		existing.setTimelineEnd(dto.getTimelineEnd());
		existing.setStatus(dto.getStatus());
		return mapToDto(repository.save(existing));
	}

	// DeleteById
	public void delete(String id) {
		repository.deleteById(id);
	}

	// UpdateStatusById
	public OtherProposalResponseDto updateStatus(String id, String status) {
		OtherProposal proposal = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Proposal not found with id: " + id));

		String newStatus = status.toUpperCase();
		switch (newStatus) {
		case "DRAFT":
		case "SUBMITTED":
		case "APPROVED":
		case "REJECTED":
			proposal.setStatus(newStatus);
			break;
		default:
			throw new IllegalArgumentException("Invalid status: " + newStatus);
		}

		OtherProposal saved = repository.save(proposal);
		return mapToDto(saved);
	}

	// Filter
	public List<OtherProposalResponseDto> filterProposals(Long entityId, String status, String proposalDescription) {
		List<OtherProposal> proposals;

		if (status == null && proposalDescription == null) {
			proposals = repository.findByCreatedBy(entityId);
		} else if (status != null && proposalDescription == null) {
			proposals = repository.findByCreatedByAndStatusIgnoreCase(entityId, status);
		} else if (status == null) {
			proposals = repository.findByCreatedByAndProposalDescriptionContainingIgnoreCase(entityId,
					proposalDescription);
		} else {
			proposals = repository.findByCreatedByAndStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(
					entityId, status, proposalDescription);
		}
		return proposals.stream().map(this::mapToDto).collect(Collectors.toList());
	}

}
