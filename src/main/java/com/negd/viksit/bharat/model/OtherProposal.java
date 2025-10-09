package com.negd.viksit.bharat.model;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.GovernmentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "other_proposals", schema = "vb_core")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtherProposal extends Auditable<Long>{

    @Id
    private String id;
    
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ministry_id", nullable = false)
//    private Ministry ministry;
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id", nullable = false)
    private GovernmentEntity ministry;
    
    @Column(nullable = false)
    private String ideaProposalTitle;

    private String proposalDescription;

    private String proposalType;

    @Column(nullable = false, length = 2000)
    private String potentialEconomicDevelopment;

    @Column(nullable = false, length = 2000)
    private String potentialEmploymentGeneration;

    @Column(nullable = false)
    private LocalDate timelineStart;

    @Column(nullable = false)
    private LocalDate timelineEnd;
    
	@Column(name = "status", nullable = false)
    private String status;
    
}
