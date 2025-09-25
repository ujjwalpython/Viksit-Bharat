package com.negd.viksit.bharat.model;


import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.util.GoalIdHelper;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "vb_goals",schema = "vb_core")
public class Goal extends Auditable<Long> {

    @Id
    private String id;

    @Column(nullable = false)
    private UUID ministryId;

    @Column(length = 500)
    private String goalDescription;

    @Column(length = 500)
    private String status;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Intervention> interventions;

    @PrePersist
    public void generateId() {
        long nextNumber = GoalIdHelper.getNextGoalNumber();
        this.id = String.format("MOCVBG%02d", nextNumber);
    }
}


