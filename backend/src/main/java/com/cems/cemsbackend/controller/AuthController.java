package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.AuthService;
import com.cems.cemsbackend.service.AuthService.AuthException;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller class that provides REST endpoints for user authentication.
 * This includes handling registration and login operations.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

  /**
   * The authentication service used to process business logic.
   */
  private final AuthService authService;

  /**
   * Constructs an AuthController with the specified AuthService.
   *
   * @param authService the service used for authentication processing.
   */
  public AuthController(final AuthService authService) {
    this.authService = authService;
  }

  /**
   * Registers a new user based on the provided request body.
   *
   * @param body the registration details provided by the user.
   * @return a ResponseEntity indicating success with a 201 status.
   */
  @PostMapping(path = "/register")
  public ResponseEntity<?> register(
          @RequestBody @Valid final RegisterRequestDTO body) {
    try {
      authService.register(body);
      return ResponseEntity.status(HttpStatus.CREATED).body(null);
    } catch (AuthException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /**
   * Authenticates a user and returns a response containing tokens.
   *
   * @param body the login credentials.
   * @return the authentication response data.
   */
  @PostMapping(path = "/login")
  public AuthResponseDTO login(
          @RequestBody @Valid final LoginRequestDTO body) {
    try {
      return authService.login(body);
    } catch (AuthException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}