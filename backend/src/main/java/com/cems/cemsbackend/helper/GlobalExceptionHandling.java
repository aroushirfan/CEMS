package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.service.CaseConversionService;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.PropertyNamingStrategy;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {

    private final CaseConversionService caseConversionService;

    public GlobalExceptionHandling(CaseConversionService caseConversionService) {
        this.caseConversionService = caseConversionService;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> errorResponse(
            ResponseStatusException responseStatusException
    ) {
        return ResponseEntity
                .status(responseStatusException.getStatusCode())
                .body(Map.of(
                        "error", responseStatusException.getReason() != null ? responseStatusException.getReason() : "No error message.")
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> bodyInvalidException(
            MethodArgumentNotValidException ex
    ) {
        StringBuilder builder = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            builder.append(String.format(", %s: %s", caseConversionService.camelToSnake(error.getField()), error.getDefaultMessage()));
        });
        builder.delete(0, 2);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", builder.toString()));
    }

}
