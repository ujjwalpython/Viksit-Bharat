package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.model.master.Ministry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Entity
@Table(name = "project_scheme", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE vb_core.project_scheme SET deleted_at = now() WHERE seq_num = ?")
@Where(clause = "deleted_at IS NULL")
public class ProjectScheme extends Auditable<Long> {

    @Column(unique = true, nullable = false)
    private String entityId;

    @Column(nullable = false)
    private String name;

    @Id
    @Column(name = "seq_num", insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheme_seq_gen")
    @SequenceGenerator(name = "scheme_seq_gen", sequenceName = "vb_core.scheme_seq", allocationSize = 1)
    private Long seqNum;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministry_id")
    private Ministry ministry;

    @Column(length = 1000)
    private String description;

    private String status;

    private LocalDate targetDate;

    private BigDecimal totalBudgetRequired;

    private Integer beneficiariesNo;

    @OneToMany(mappedBy = "projectScheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchemeKeyDeliverable> keyDeliverables;

    @PrePersist
    public void generateId() {
        if (this.seqNum != null) {
            this.entityId = String.format("PROJ%02d", this.seqNum);
        }
    }
}

