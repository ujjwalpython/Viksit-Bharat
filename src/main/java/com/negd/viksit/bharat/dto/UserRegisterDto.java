package com.negd.viksit.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String mobileNumber;
    private String userType;
    private UUID ministryId;
    private UUID departmentId;
}

