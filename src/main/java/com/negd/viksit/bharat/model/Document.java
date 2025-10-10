package com.negd.viksit.bharat.model;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE vb_core.documents SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "documents", schema = "vb_core")
public class Document extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String fileName; 

    @Column
    private String fileType;

    @Column
    private String fileUrl;

    @Column
    private Long size;

    @Column
    private String referenceType;

    @Column
    private String referenceId;
}
