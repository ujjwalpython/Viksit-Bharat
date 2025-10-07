package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "reform_milestone", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE vb_core.reform_milestone SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
public class ReformMilestone extends Auditable<Long> {

    @Column(unique = true, nullable = false)
    private String entityId;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "milestone_seq_gen")
    @SequenceGenerator(name = "milestone_seq_gen", sequenceName = "vb_core.milestone_seq", allocationSize = 1)
    private Long seqNum;

    private String activityDescription;
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reform_id")
    private RegulatoryReform reform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    private Integer sortOrder;

    @PrePersist
    public void generateId() {
        if (this.seqNum != null) {
            this.entityId = String.format("RFML%02d", this.seqNum);
        }
    }
}

