package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.service.CaseConversionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlingTest {

  @Test
  void errorResponse_ShouldReturnReasonMessage() {
    GlobalExceptionHandling handler = new GlobalExceptionHandling(Mockito.mock(CaseConversionService.class));

    ResponseStatusException ex =
            new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid request");

    ResponseEntity<?> response = handler.errorResponse(ex);

    assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(Map.of("error", "Invalid request"));
  }

  @Test
  void errorResponse_ShouldReturnDefaultMessage_WhenReasonIsNull() {
    GlobalExceptionHandling handler = new GlobalExceptionHandling(Mockito.mock(CaseConversionService.class));

    ResponseStatusException ex =
            new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, null);

    ResponseEntity<?> response = handler.errorResponse(ex);

    assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo(Map.of("error", "No error message."));
  }

  @Test
  void bodyInvalidException_ShouldReturnSnakeCaseErrors() {
    // Mock CaseConversionService
    CaseConversionService caseService = Mockito.mock(CaseConversionService.class);
    when(caseService.camelToSnake("firstName")).thenReturn("first_name");
    when(caseService.camelToSnake("phoneNumber")).thenReturn("phone_number");

    GlobalExceptionHandling handler = new GlobalExceptionHandling(caseService);

    // Create BindingResult with field errors
    Object target = new Object();
    BeanPropertyBindingResult bindingResult =
            new BeanPropertyBindingResult(target, "objectName");

    bindingResult.addError(new FieldError("objectName", "firstName", "must not be blank"));
    bindingResult.addError(new FieldError("objectName", "phoneNumber", "invalid format"));

    MethodArgumentNotValidException ex =
            new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<?> response = handler.bodyInvalidException(ex);

    assertThat(response.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);

    // The builder logic produces: "first_name: must not be blank, phone_number: invalid format"
    assertThat(response.getBody()).isEqualTo(
            Map.of("error", "first_name: must not be blank, phone_number: invalid format")
    );
  }
}
