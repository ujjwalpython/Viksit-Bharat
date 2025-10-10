package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.model.master.Ministry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "regulatory_reform", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE vb_core.regulatory_reform SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
public class RegulatoryReform extends Auditable<Long> {
    @Column(unique = true,nullable = false)
    private String entityId;

    private String name;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reform_seq_gen")
    @SequenceGenerator(name = "reform_seq_gen", sequenceName = "vb_core.reform_seq", allocationSize = 1)
    private Long seqNum;

    @Column(length = 1000)
    private String description;
    private String reformType;

    @ManyToOne
    @JoinColumn(name = "gov_entity_id")
    private GovernmentEntity governmentEntity;

    private LocalDate targetCompletionDate;
    private String rulesToBeAmended;
    private String intendedOutcome;
    private String presentStatus;

    private String status;
    @OneToMany(mappedBy = "reform", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReformMilestone> milestones;

    @PrePersist
    public void generateId() {
        if (this.seqNum != null) {
            this.entityId = String.format("%s%02d", this.governmentEntity.getCode()+"RGRF", this.seqNum);
        }
    }
}

