package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity operations.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

  /**
   * Finds a user by their unique identifier.
   *
   * @param uuid unique identifier
   * @return an optional containing the found user, or empty
   */
  Optional<User> getUserById(UUID uuid);

  /**
   * Checks if a user exists with the given email address.
   *
   * @param email the email to check
   * @return true if a user exists with this email
   */
  boolean existsUserByEmail(String email);

  /**
   * Finds a user by their email address.
   *
   * @param email the user email
   * @return an optional containing the found user, or empty
   */
  Optional<User> getUserByEmail(String email);
}