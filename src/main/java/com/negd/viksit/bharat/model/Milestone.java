package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "milestone", schema = "vb_core")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Milestone  extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_status_id", nullable = false)
    private GoalTargetStatus goalTargetStatus;

    @Column(nullable = false, length = 500)
    private String activityDescription;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column
    private Integer sortOrder;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    private Document document;
}
