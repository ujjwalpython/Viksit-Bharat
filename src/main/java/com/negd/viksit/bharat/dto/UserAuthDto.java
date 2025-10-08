package com.negd.viksit.bharat.dto;

import com.negd.viksit.bharat.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserAuthDto {

    private Long userId;
    private String firstName;
    private Set<String> roles;
    private String email;
    private String authToken;
    private String ministry;

    public UserAuthDto(User user, String token) {
        this.userId = user.getEntityid();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.authToken = token;
        this.roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        if (user.hasRole("DEPT_ADMIN","MINISTRY_ADMIN")){
            this.ministry = user.getGovernmentEntity().getName();}
        }
}
