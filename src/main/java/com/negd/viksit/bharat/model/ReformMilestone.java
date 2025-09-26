package com.negd.viksit.bharat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "reform_milestone")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReformMilestone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String activityDescription;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "reform_id")
    private RegulatoryReform reform;

    private Integer sortOrder;
}

