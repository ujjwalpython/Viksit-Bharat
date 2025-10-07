package com.negd.viksit.bharat.exception;



import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                        HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation failed at [{}] with errors: {}", request.getRequestURI(), errors);

        return ResponseGenerator.error(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request,
                errors
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex,
                                                 HttpServletRequest request) {
        String message = "Missing parameter: " + ex.getParameterName();
        log.warn("Missing request parameter at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex,
                                                  HttpServletRequest request) {
        log.warn("Bad credentials attempt at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.UNAUTHORIZED, "Invalid username or password", request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UsernameNotFoundException ex,
                                                HttpServletRequest request) {
        log.warn("User not found at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedAccess(UnauthorizedException ex,
                                                      HttpServletRequest request) {
        log.warn("Unauthorized access at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBusinessException(BadRequestException ex,
                                                     HttpServletRequest request) {
        log.warn("Bad request at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex,
                                                  HttpServletRequest request) {
        log.warn("Entity not found at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExists(EntityExistsException ex,
                                                HttpServletRequest request) {
        log.warn("Entity already exists at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<?> handleInvalidStatus(InvalidStatusException ex,
                                                 HttpServletRequest request) {
        log.warn("Invalid status at [{}]: {}", request.getRequestURI(), ex.getMessage());
        return ResponseGenerator.error(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex,
                                       HttpServletRequest request) {
        log.error("Unexpected error at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseGenerator.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }
}
