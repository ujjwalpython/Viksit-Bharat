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
@Table(name = "scheme_key_deliverables", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE vb_core.scheme_key_deliverables SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
public class SchemeKeyDeliverable extends Auditable<Long> {

    @Column(unique = true, nullable = false)
    private String entityId;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheme_deliverable_seq_gen")
    @SequenceGenerator(name = "scheme_deliverable_seq_gen", sequenceName = "vb_core.scheme_deliverable_seq", allocationSize = 1)
    private Long seqNum;

    @Column(nullable = false, length = 1000)
    private String activityDescription;

    private LocalDate deadline;

    @Column(length = 2000)
    private String progressMade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_scheme_id")
    private ProjectScheme projectScheme;

    @PrePersist
    public void generateId() {
        if (this.seqNum != null) {
            this.entityId = String.format("SCKD%02d", this.seqNum);
        }
    }
}

