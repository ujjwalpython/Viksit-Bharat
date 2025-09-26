package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "goal_target_status", schema = "vb_core")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalTargetStatus extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 500)
    private String target;

    @Column(nullable = false, length = 500)
    private String actionPoint;

    @Column(name = "achievement_status", nullable = false, length = 100)
    private String presentStatusOfAchievement;

    @Column(length = 1000)
    private String bottlenecks;

    @Column(length = 50)
    private String priority;

    @Column(length = 500)
    private String status;

    @Column(nullable = false)
    private LocalDate completionDate;

    @OneToMany(mappedBy = "goalTargetStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> milestones;
}
