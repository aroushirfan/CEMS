package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.UserService;
import com.cems.shared.model.UserDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing user accounts and administrative access levels.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  /** The service handling user logic. */
  private final UserService userService;

  /**
   * Constructs a new UserController.
   *
   * @param userService the user service to be injected.
   */
  public UserController(final UserService userService) {
    this.userService = userService;
  }

  /**
   * Retrieves all users in the system.
   *
   * @return a list of all user DTOs.
   */
  @GetMapping
  public List<UserDTO> getAllUsers() {
    return userService.getAllUsers();
  }

  /**
   * Retrieves a specific user by their unique identifier.
   *
   * @param userId the UUID of the user to fetch.
   * @return the found user DTO.
   */
  @GetMapping("/{id}")
  public UserDTO getUserById(@PathVariable("id") final UUID userId) {
    return userService.getUserById(userId);
  }

  /**
   * Updates the access level of a specific user.
   *
   * @param userId the UUID of the user to update.
   * @param userDto the data containing the new access level.
   */
  @PutMapping("/{id}/access-level")
  public void updateAccessLevel(
          @PathVariable("id") final UUID userId,
          @RequestBody final UserDTO userDto
  ) {
    userService.updateAccessLevel(userId, userDto.getAccessLevel());
  }

  /**
   * Retrieves the profile data of the currently authenticated user.
   *
   * @param auth the authentication token of the current session.
   * @return the DTO of the current user.
   */
  @GetMapping("/me")
  public UserDTO getCurrentUser(final Authentication auth) {
    final UUID userId = UUID.fromString(auth.getName());
    return userService.getUserById(userId);
  }

  /**
   * Updates the profile data of the currently authenticated user.
   *
   * @param auth the authentication token of the current session.
   * @param userDto the updated user information.
   * @return the updated user DTO.
   */
  @PutMapping("/me")
  public UserDTO updateCurrentUser(
          final Authentication auth,
          @RequestBody final UserDTO userDto
  ) {
    final UUID userId = UUID.fromString(auth.getName());
    return userService.updateCurrentUser(userId, userDto);
  }

  /**
   * Deletes the account of the currently authenticated user.
   *
   * @param auth the authentication token of the current session.
   */
  @DeleteMapping("/me")
  public void deleteCurrentUser(final Authentication auth) {
    final UUID userId = UUID.fromString(auth.getName());
    userService.deleteUser(userId);
  }
}