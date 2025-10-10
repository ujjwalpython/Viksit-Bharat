package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@SQLDelete(sql = "UPDATE vb_core.vb_goal_interventions SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "vb_goal_interventions",schema = "vb_core")
public class GoalIntervention extends Auditable<Long> {

    @Column(nullable = false)
    private String entityId;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goal_intervention_seq_gen")
    @SequenceGenerator(name = "goal_intervention_seq_gen", sequenceName = "vb_core.goal_intervention_seq", allocationSize = 1)
    private Long seqNum;

    @Column(nullable = false,length = 500)
    private String targetDescription;

    private String presentValue;
    private String presentUnit;
    private Integer presentYear;
    private String target2030Value;
    private String target2030Unit;
    private String target2047Unit;
    private String target2047Value;
    private Integer sortOrder;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

}

