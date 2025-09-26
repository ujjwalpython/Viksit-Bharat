package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.ProposalDto;
import com.negd.viksit.bharat.model.Proposal;
import com.negd.viksit.bharat.repository.ProposalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalService {

	private final ProposalRepository repository;

	public ProposalService(ProposalRepository repository) {
		this.repository = repository;
	}

	private ProposalDto mapToDto(Proposal entity) {
		return ProposalDto.builder().id(entity.getId()).ideaProposalTitle(entity.getIdeaProposalTitle())
				.proposalDescription(entity.getProposalDescription()).proposalType(entity.getProposalType())
				.potentialEconomicDevelopment(entity.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(entity.getPotentialEmploymentGeneration())
				.timelineStart(entity.getTimelineStart()).timelineEnd(entity.getTimelineEnd())
				.formStatus(entity.getFormStatus()).build();
	}

	private Proposal mapToEntity(ProposalDto dto) {
		return Proposal.builder().id(dto.getId()).ideaProposalTitle(dto.getIdeaProposalTitle())
				.proposalDescription(dto.getProposalDescription()).proposalType(dto.getProposalType())
				.potentialEconomicDevelopment(dto.getPotentialEconomicDevelopment())
				.potentialEmploymentGeneration(dto.getPotentialEmploymentGeneration())
				.timelineStart(dto.getTimelineStart()).timelineEnd(dto.getTimelineEnd()).formStatus(dto.getFormStatus())
				.build();
	}

	public ProposalDto create(ProposalDto dto) {
		Proposal saved = repository.save(mapToEntity(dto));
		return mapToDto(saved);
	}

	public List<ProposalDto> getAll() {
		return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public ProposalDto getOne(Long id) {
		return repository.findById(id).map(this::mapToDto)
				.orElseThrow(() -> new RuntimeException("Proposal not found"));
	}

	public ProposalDto update(Long id, ProposalDto dto) {
		Proposal existing = repository.findById(id).orElseThrow(() -> new RuntimeException("Proposal not found"));
		existing.setIdeaProposalTitle(dto.getIdeaProposalTitle());
		existing.setProposalDescription(dto.getProposalDescription());
		existing.setProposalType(dto.getProposalType());
		existing.setPotentialEconomicDevelopment(dto.getPotentialEconomicDevelopment());
		existing.setPotentialEmploymentGeneration(dto.getPotentialEmploymentGeneration());
		existing.setTimelineStart(dto.getTimelineStart());
		existing.setTimelineEnd(dto.getTimelineEnd());
		return mapToDto(repository.save(existing));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}

	public ProposalDto updateStatus(Long id, String status) {
		Proposal proposal = repository.findById(id)
				.orElseThrow(() -> new RuntimeException("Proposal not found with id: " + id));

		String newStatus = status.toUpperCase();
		switch (newStatus) {
		case "DRAFT":
		case "SUBMITTED":
		case "APPROVED":
		case "REJECTED":
			proposal.setFormStatus(newStatus);
			break;
		default:
			throw new IllegalArgumentException("Invalid status: " + newStatus);
		}

		Proposal saved = repository.save(proposal);
		return mapToDto(saved);
	}

}
