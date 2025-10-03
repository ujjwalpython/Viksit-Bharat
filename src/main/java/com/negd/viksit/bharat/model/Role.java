package com.negd.viksit.bharat.model;


import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "role", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends Auditable<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    private String description;
}

