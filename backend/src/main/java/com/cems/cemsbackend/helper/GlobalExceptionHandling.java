package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.service.CaseConversionService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global advisor for handling exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandling {

  private final CaseConversionService caseConversionService;

  /**
   * Constructor for GlobalExceptionHandling.
   *
   * @param caseConversionService service for string case transformation.
   */
  public GlobalExceptionHandling(CaseConversionService caseConversionService) {
    this.caseConversionService = caseConversionService;
  }

  /**
   * Handles ResponseStatusException and returns a formatted JSON error.
   *
   * @param ex the exception thrown by the controller.
   * @return a response entity containing the error message.
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<?> errorResponse(ResponseStatusException ex) {
    String message = ex.getReason() != null ? ex.getReason() : "No error message.";
    return ResponseEntity
        .status(ex.getStatusCode())
        .body(Map.of("error", message));
  }

  /**
   * Handles validation errors from request bodies and returns a snake_case field map.
   *
   * @param ex the validation exception.
   * @return a response entity with summarized validation errors.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> bodyInvalidException(MethodArgumentNotValidException ex) {
    StringBuilder builder = new StringBuilder();
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      String field = caseConversionService.camelToSnake(error.getField());
      builder.append(String.format(", %s: %s", field, error.getDefaultMessage()));
    });
    builder.delete(0, 2);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", builder.toString()));
  }
}