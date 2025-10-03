package com.negd.viksit.bharat.controller;


import com.negd.viksit.bharat.dto.UserAuthDto;
import com.negd.viksit.bharat.dto.UserRegisterDto;
import com.negd.viksit.bharat.dto.UserRegisterResDto;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.service.LoginService;
import com.negd.viksit.bharat.service.TokenService;
import com.negd.viksit.bharat.service.UserService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController

@RequestMapping("/api/v1/auth")
public class AuthController {


    private final TokenService tokenService;
    private final UserService userService;
    private LoginService loginService;

    public AuthController(LoginService authenticationService, TokenService tokenService, UserService userService) {
        this.loginService = authenticationService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody @Valid com.negd.viksit.bharat.dto.UserCredentialDto userCredentials,
            HttpServletRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException {

        UserAuthDto userAuthDto = loginService.authenticate(userCredentials.email(), userCredentials.password());

        return ResponseGenerator.created(
                userAuthDto,
                "User authenticated successfully",
                request
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {
        tokenService.logout(user);
        return ResponseGenerator.success(
                null, "User logged out successfully",
                request
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody @Valid UserRegisterDto requestDto,
            HttpServletRequest request) {

        UserRegisterResDto response = userService.registerUser(requestDto);

        return ResponseGenerator.created(
                response,
                "User registered successfully",
                request
        );
    }

}
