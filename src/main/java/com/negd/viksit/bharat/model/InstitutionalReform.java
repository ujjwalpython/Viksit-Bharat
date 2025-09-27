package com.negd.viksit.bharat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.negd.viksit.bharat.audit.Auditable;
import com.negd.viksit.bharat.enums.ReformType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Persistence;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "institutional_reform", schema = "vb_core")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionalReform extends Auditable<Long> {

	@Id
    private String id;
//	private String goalId;
    private String institutionalReformName;
    private String reformDescription;

//    @Enumerated(EnumType.STRING)
//    private ReformType reformType;
    private String reformType;

    private LocalDate targetCompletionDate;
    
    private String rulesToBeAmended;

    private String intendedOutcome;

    private String presentStatus;
    
    private String status;
    
    @OneToMany(mappedBy = "institutionalReform", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Target> target = new ArrayList<>();
    
    public void addTarget(Target deliverable) {
        deliverable.setInstitutionalReform(this);
        this.target.add(deliverable);
    }

    public void removeTarget(Target deliverable) {
        deliverable.setInstitutionalReform(null);
        this.target.remove(deliverable);
    }

//    @ManyToOne
//    @JoinColumn(name = "target_id")
//    private Target target;
    
  
}
