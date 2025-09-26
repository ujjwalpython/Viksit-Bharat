package com.negd.viksit.bharat.model;

import java.time.LocalDate;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institutional_reform", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionalReform extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "reform_type")
    private String reformType;

    @Column(name = "target_completion_date")
    private LocalDate targetCompletionDate;

    @Column(name = "rules_to_be_amended")
    private String rulesToBeAmended;

    @Column(name = "intended_outcome")
    private String intendedOutcome;

    @Column(name = "present_status")
    private String presentStatus;
    
    @Column(name = "form_status")
    private String formStatus;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Target target;
}
