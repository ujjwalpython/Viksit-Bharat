package com.negd.viksit.bharat.model.master;

import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "target_indicator",schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TargetIndicator extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = true;
}

