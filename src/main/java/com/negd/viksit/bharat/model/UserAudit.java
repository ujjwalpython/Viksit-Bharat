package com.negd.viksit.bharat.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "user_audit", schema = "authentication")
public class UserAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer entityid;

    private String requested_token;
    private boolean is_logout;
}
