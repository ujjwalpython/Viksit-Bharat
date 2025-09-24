package com.negd.viksit.bharat.model;


import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "goals")
public class Goal extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID ministryId;

    @Column(length = 500)
    private String goalDescription;

    @Column(length = 500)
    private String status;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Intervention> interventions;
}


