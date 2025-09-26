package com.negd.viksit.bharat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.dto.ProposalDto;
import com.negd.viksit.bharat.model.InstitutionalReform;
import com.negd.viksit.bharat.model.Proposal;
import com.negd.viksit.bharat.repository.ProposalRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ProposalService {

	private final ProposalRepository repository;

	public ProposalService(ProposalRepository repository) {
		this.repository = repository;
	}
	
	 private static final String ID_PREFIX = "MOCVBIP";

	    @PersistenceContext
	    private EntityManager entityManager;

	    @Transactional
	    public Proposal save(Proposal reform) {
	        if (reform.getId() == null || reform.getId().isEmpty()) {
	            reform.setId(generateCustomId());
	        }
	        return repository.save(reform);
	    }

	    private String generateCustomId() {
	        // Native query to find last ID starting with prefix ordered descending
	        String sql = "SELECT ir.id FROM vb_core.institutional_reform ir WHERE ir.id LIKE :prefix ORDER BY ir.id DESC LIMIT 1";
//	    	String sql = "SELECT ir.id FROM institutional_reform ir WHERE ir.id LIKE :prefix ORDER BY ir.id DESC LIMIT 1";
	        List<String> result = entityManager.createNativeQuery(sql)
	                .setParameter("prefix", ID_PREFIX + "%")
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

	private ProposalDto mapToDto(Proposal entity) {
		return ProposalDto.builder().id(entity.getId()).goalId(entity.getGoalId()).ideaProposalTitle(entity.getIdeaProposalTitle())
				.proposalDescription(entity.getProposalDescription()).proposalType(entity.getProposalType())
				.potentialEconomicDevelopment(entity.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(entity.getPotentialEmploymentGeneration())
				.timelineStart(entity.getTimelineStart()).timelineEnd(entity.getTimelineEnd())
				.status(entity.getStatus()).build();
	}

	private Proposal mapToEntity(ProposalDto dto) {
		return Proposal.builder().id(dto.getId()).goalId(dto.getGoalId()).ideaProposalTitle(dto.getIdeaProposalTitle())
				.proposalDescription(dto.getProposalDescription()).proposalType(dto.getProposalType())
				.potentialEconomicDevelopment(dto.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(dto.getPotentialEmploymentGeneration())
				.timelineStart(dto.getTimelineStart()).timelineEnd(dto.getTimelineEnd()).status(dto.getStatus())
				.build();
	}
	
	 public ProposalDto create(ProposalDto dto) {
	        Proposal entity = mapToEntity(dto);
	        Proposal saved = save(entity);
	        return mapToDto(saved);
	    }

	public List<ProposalDto> getAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public ProposalDto getOne(String id) {
		return repository.findById(id).map(this::mapToDto)
				.orElseThrow(() -> new RuntimeException("Proposal not found"));
	}

	public ProposalDto update(String id, ProposalDto dto) {
		Proposal existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Proposal not found"));
		existing.setIdeaProposalTitle(dto.getIdeaProposalTitle());
		existing.setGoalId(dto.getGoalId());
		existing.setProposalDescription(dto.getProposalDescription());
		existing.setProposalType(dto.getProposalType());
		existing.setPotentialEconomicDevelopment(dto.getPotentialEconomicDevelopment());
		existing.setPotentialEmploymentGeneration(dto.getPotentialEmploymentGeneration());
		existing.setTimelineStart(dto.getTimelineStart());
		existing.setTimelineEnd(dto.getTimelineEnd());
		return mapToDto(repository.save(existing));
	}

	public void delete(String id) {
		repository.deleteById(id);
	}

	public ProposalDto updateStatus(String id, String status) {
		Proposal proposal = repository.findById(id)
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

		Proposal saved = repository.save(proposal);
		return mapToDto(saved);
	}

	public List<ProposalDto> filterProposals(Long entityId, String status, String proposalDescription) {
	    List<Proposal> proposals;

	    if (status == null && proposalDescription == null) {
	        proposals = repository.findByCreatedBy(entityId);
	    } else if (status != null && proposalDescription == null) {
	        proposals = repository.findByCreatedByAndStatusIgnoreCase(entityId, status);
	    } else if (status == null) {
	        proposals = repository.findByCreatedByAndProposalDescriptionContainingIgnoreCase(entityId, proposalDescription);
	    } else {
	        proposals = repository.findByCreatedByAndStatusIgnoreCaseAndProposalDescriptionContainingIgnoreCase(
	                entityId, status, proposalDescription
	        );
	    }

	    return proposals.stream()
	            .map(this::mapToDto)   // ✅ better: use method reference
	            .collect(Collectors.toList()); // ✅ use Collectors for consistency
	}


}
