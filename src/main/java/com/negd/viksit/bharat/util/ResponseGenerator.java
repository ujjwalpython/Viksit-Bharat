package com.negd.viksit.bharat.util;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseGenerator {

    public static ResponseEntity<?> success(Object data, String message, HttpServletRequest request) {
        return buildResponse(HttpStatus.OK, message, request, data);
    }

    public static ResponseEntity<?> created(Object data, String message, HttpServletRequest request) {
        return buildResponse(HttpStatus.CREATED, message, request, data);
    }

    public static ResponseEntity<?> error(HttpStatus status, String message, HttpServletRequest request) {
        return buildResponse(status, message, request, null);
    }

    public static ResponseEntity<?> error(HttpStatus status, String message, HttpServletRequest request, Object errors) {
        return buildResponse(status, message, request, errors);
    }

    private static ResponseEntity<?> buildResponse(HttpStatus status, String message,
                                                   HttpServletRequest request, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        body.put("data", data);
        return new ResponseEntity<>(body, status);
    }
}

