package com.negd.viksit.bharat.util;


import com.negd.viksit.bharat.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseGenerator {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, HttpServletRequest request) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message(message)
                .path(request != null ? request.getRequestURI() : null)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message, HttpServletRequest request) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .message(message)
                .path(request != null ? request.getRequestURI() : null)
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message, String error, HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request != null ? request.getRequestURI() : null)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}

