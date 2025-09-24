package com.negd.viksit.bharat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.negd.viksit.bharat.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_entity", schema = "authentication")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable<Integer> implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long entityid;

    @Column(length = 50, nullable = true)
    private String firstName;

    @Column(length = 50, nullable = true)
    private String middleName;

    @Column(length = 50, nullable = true)
    private String lastName;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "mno")
    private String mobileNumber;

    @Column(name = "state", nullable = true)
    private String state;

    @Column(name = "district", nullable = true)
    private String district;

    @Column(name = "pincode", nullable = true)
    // @Pattern(regexp = "^[1-9]{1}[0-9]{2}[0-9]{3}$")
    private String pincode;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "user_type", nullable = true)
    private String user_type;

    @JsonIgnore
    @Column(name = "usertype", nullable = true)
    private String userType;

    @JsonIgnore
    @Column(name = "status", nullable = true)
    private String status;

    @JsonIgnore
    @Column(name = "ip_address", nullable = true)
    private String ip_address;


    @Column(nullable = true)
    @JsonProperty(access = Access.READ_ONLY)
    private Integer failedAttemptCount;


    @Column(name = "is_active")
    private Boolean is_active;

    @Column(name = "is_delete")
    private Boolean is_delete;

    @Column(name = "security_question", nullable = true)
    private String security_question;

    @Column(name = "security_answer", nullable = true)
    private String security_answer;

    @Column(name = "lastpasswordresetdate", nullable = true)
    private Long lastPasswordResetDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

