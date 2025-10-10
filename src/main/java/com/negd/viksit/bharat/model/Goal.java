package com.negd.viksit.bharat.model;


import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.model.master.Ministry;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Data
@Entity
@SQLDelete(sql = "UPDATE vb_core.vb_goals SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "vb_goals",schema = "vb_core")
public class Goal extends Auditable<Long> {

    @Column(unique = true,nullable = false)
    private String entityId;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goal_seq_gen")
    @SequenceGenerator(name = "goal_seq_gen", sequenceName = "vb_core.goal_seq", allocationSize = 1)
    private Long seqNum;

    @ManyToOne
    @JoinColumn(name = "gov_entity_id")
    private GovernmentEntity governmentEntity;

    @Column(length = 500)
    private String goalDescription;

    @Column(length = 500)
    private String status;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalIntervention> interventions;

    @PrePersist
    public void generateId() {
        if (this.seqNum != null) {
            this.entityId = String.format("%s%02d", this.governmentEntity.getCode()+"G", this.seqNum);
        }
    }
}


