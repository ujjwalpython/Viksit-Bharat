package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.UserAuthDto;
import com.negd.viksit.bharat.dto.UserPrincipal;
import com.negd.viksit.bharat.exception.UnauthorizedException;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.repository.UserRepository;
import com.negd.viksit.bharat.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserAuthDto authenticate(String email, String password) {
        try {
            log.debug("Attempting authentication with email: {}", email);

            // Debugging: check DB password and match
            userRepository.findByEmail(email).ifPresent(u -> {
                log.info("Encoded password from DB: {}", u.getPassword());
                log.info("Password matches raw '{}': {}", password, encoder.matches(password, u.getPassword()));
            });

            // Perform Spring Security authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Since User implements UserDetails, authentication.getPrincipal() will be your User entity
            User loggedInUser = (User) authentication.getPrincipal();

            // Generate JWT
            String token = jwtTokenUtil.generateToken(loggedInUser);

            return new UserAuthDto(loggedInUser, token);

        } catch (Exception e) {
            log.error("Authentication failed for email: {}, error: {}", email, e.getMessage());
            throw new UnauthorizedException("Unauthorized access");
        }
    }
}
