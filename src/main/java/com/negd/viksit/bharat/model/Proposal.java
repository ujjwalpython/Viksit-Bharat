package com.negd.viksit.bharat.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

@Entity
@Table(name = "proposals", schema = "vb_core")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proposal extends Auditable<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
