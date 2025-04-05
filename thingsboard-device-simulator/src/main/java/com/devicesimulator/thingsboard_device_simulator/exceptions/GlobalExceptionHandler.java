package com.devicesimulator.thingsboard_device_simulator.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {
        log.error("Handled API exception [{}]: {}", ex.getErrorCode(), ex.getMessage());
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Optionally handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception: ", ex);
        ApiErrorResponse response = new ApiErrorResponse("An unexpected error occurred", "INTERNAL_ERROR");
        return ResponseEntity.internalServerError().body(response);
    }
}