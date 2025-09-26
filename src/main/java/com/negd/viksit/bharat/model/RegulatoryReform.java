package com.negd.viksit.bharat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity(name = "regulatory_reform")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegulatoryReform {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;
    private String reformType;
    private LocalDate targetCompletionDate;
    private String rulesToBeAmended;
    private String intendedOutcome;
    private String presentStatus;

    private String status;
    private Long createdBy;

    @OneToMany(mappedBy = "reform", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReformMilestone> milestones;
}

