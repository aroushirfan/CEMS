package com.cems.cemsbackend.helper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> errorResponse(
            ResponseStatusException responseStatusException
    ) {
        return ResponseEntity
                .status(responseStatusException.getStatusCode())
                .body(Map.of(
                        "error", responseStatusException.getReason() != null ? responseStatusException.getReason() : "")
                );
    }

}
