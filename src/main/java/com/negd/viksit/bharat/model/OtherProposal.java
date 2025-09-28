package com.negd.viksit.bharat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.Ministry;

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
    
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id")
    private Ministry ministry;
    
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
    
    @Column(name = "status")
    private String status;
    
}
