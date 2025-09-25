package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "vb_goal_interventions",schema = "vb_core")
public class Intervention extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID targetDescriptionId;

    @Column(nullable = false, length = 255)
    private String targetDescription;

    private Double presentValue;
    private Integer presentYear;
    private Double target2030Value;
    private Double target2047Value;
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

}

