package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "vb_goal_interventions",schema = "vb_core")
public class GoalIntervention extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String targetDescription;

    private String presentValue;
    private String presentUnit;
    private Integer presentYear;
    private String target2030Value;
    private String target2030Unit;
    private String target2047Unit;
    private String target2047Value;
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

}

