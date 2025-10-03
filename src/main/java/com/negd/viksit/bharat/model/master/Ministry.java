package com.negd.viksit.bharat.model.master;

import java.util.List;
import java.util.UUID;

import com.negd.viksit.bharat.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ministry_master",schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ministry extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_deleted")
    private Boolean isDeleted = true;
    
    @OneToMany(mappedBy = "ministry", fetch = FetchType.LAZY)
    private List<Department> departments;

}

