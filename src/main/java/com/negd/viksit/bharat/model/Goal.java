package com.negd.viksit.bharat.model;


import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.util.GoalIdHelper;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@SQLDelete(sql = "UPDATE vb_core.vb_goals SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "vb_goals",schema = "vb_core")
public class Goal extends Auditable<Long> {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id")
    private Ministry ministry;

    @Column(length = 500)
    private String goalDescription;

    @Column(length = 500)
    private String status;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalIntervention> interventions;

    @PrePersist
    public void generateId() {
        long nextNumber = GoalIdHelper.getNextGoalNumber();
        this.id = String.format("MOCVBG%02d", nextNumber);
    }
}


