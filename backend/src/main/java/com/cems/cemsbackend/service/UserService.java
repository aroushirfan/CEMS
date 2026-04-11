package com.cems.cemsbackend.service;

import com.cems.cemsbackend.mappers.UserMapper;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.UserDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Service handling business logic for User operations.
 */
@Service
public class UserService {

  /** Error message for missing users. */
  private static final String NOT_FOUND = "User not found";

  /** Repository for user data persistence. */
  private final UserRepository userRepository;

  /**
   * Constructs the UserService.
   *
   * @param userRepository repository for user data
   */
  public UserService(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves all users in the system.
   *
   * @return list of user DTOs
   */
  public List<UserDTO> getAllUsers() {
    return userRepository.findAll()
            .stream()
            .map(UserMapper::toDTO)
            .toList();
  }

  /**
   * Retrieves a specific user by their ID.
   *
   * @param uuid unique identifier of the user
   * @return user data transfer object
   * @throws RuntimeException if user is not found
   */
  public UserDTO getUserById(final UUID uuid) {
    return userRepository.findById(uuid)
            .map(UserMapper::toDTO)
            .orElseThrow(() -> new RuntimeException(NOT_FOUND));
  }

  /**
   * Retrieves a user by their email address.
   *
   * @param email user email address
   * @return user data transfer object
   * @throws RuntimeException if user is not found
   */
  public UserDTO getUserByEmail(final String email) {
    final User user = userRepository.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException(NOT_FOUND));
    return UserMapper.toDTO(user);
  }

  /**
   * Updates the access level of a user.
   *
   * @param uuid unique identifier of the user
   * @param newLevel the new access level integer
   * @throws RuntimeException if user is not found
   */
  public void updateAccessLevel(final UUID uuid, final int newLevel) {
    final User user = userRepository.findById(uuid)
            .orElseThrow(() -> new RuntimeException(NOT_FOUND));

    user.setAccessLevel(newLevel);
    userRepository.save(user);
  }

  /**
   * Updates the profile information for the current user.
   *
   * @param userUuid unique identifier of the user
   * @param dto data transfer object with new info
   * @return updated user data transfer object
   * @throws RuntimeException if user is not found
   */
  public UserDTO updateCurrentUser(final UUID userUuid, final UserDTO dto) {
    final User user = userRepository.findById(userUuid)
            .orElseThrow(() -> new RuntimeException(NOT_FOUND));

    UserMapper.updateEntity(user, dto);
    userRepository.save(user);

    return UserMapper.toDTO(user);
  }

  /**
   * Deletes a user from the system.
   *
   * @param uuid unique identifier of the user
   */
  public void deleteUser(final UUID uuid) {
    userRepository.deleteById(uuid);
  }
}