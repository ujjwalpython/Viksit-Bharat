package com.negd.viksit.bharat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

@Entity
@Table(name = "other_proposals", schema = "vb_core")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proposal extends Auditable<Long>{

    @Id
    private String id;
    
    private String goalId;
    
    @Column(nullable = false)
    private String ideaProposalTitle;

    private String proposalDescription;

    private String proposalType; // Policy / Technology

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
