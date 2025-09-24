package com.negd.viksit.bharat.controller;


import com.negd.viksit.bharat.dto.UserAuthDto;
import com.negd.viksit.bharat.service.LoginService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController

@RequestMapping("/api/v1/oauth")
public class AuthController {


    private LoginService loginService;

    public AuthController(LoginService authenticationService) {
        this.loginService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid com.negd.viksit.bharat.dto.UserCredentialDto userCredentials) throws NoSuchAlgorithmException, InvalidKeySpecException {
        UserAuthDto userAuthDto = loginService.authenticate(userCredentials.email(), userCredentials.password());
        return new ResponseEntity<>(userAuthDto, HttpStatus.CREATED);

    }


}
