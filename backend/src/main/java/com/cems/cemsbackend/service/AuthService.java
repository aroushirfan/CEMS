package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for user authentication and registration.
 */
@Service
public class AuthService {

  /** Service used for generating and validating tokens. */
  private final JwtService jwtService;

  /** Repository used for user data access. */
  private final UserRepository userRepository;

  /** Encoder used for hashing and checking passwords. */
  private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

  /**
   * Constructs an AuthService with necessary dependencies.
   *
   * @param jwtService     the service used for JWT operations
   * @param userRepository the repository used for user data access
   */
  public AuthService(final JwtService jwtService, final UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  /**
   * Custom exception for authentication-related errors.
   */
  public static class AuthException extends Exception {

    /** Serial version UID for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new AuthException with a specified detail message.
     *
     * @param message the detail message
     */
    public AuthException(final String message) {
      super(message);
    }
  }

  /**
   * Registers a new user based on the provided registration details.
   *
   * @param registerRequest the DTO containing registration information
   * @throws AuthException if a user with the same email already exists
   */
  public void register(final RegisterRequestDTO registerRequest) throws AuthException {

    if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
      throw new AuthException("User already exists.");
    }

    final String hashedPassword = bcrypt.encode(registerRequest.getPassword());

    final User user = new User();
    user.setEmail(registerRequest.getEmail());
    user.setHashedPassword(hashedPassword);
    user.setAccessLevel(AccessLevel.USER);
    user.setFirstName(registerRequest.getFirstName());
    user.setMiddleName(registerRequest.getMiddleName());
    user.setLastName(registerRequest.getLastName());

    user.setPhone(null);
    user.setDob(null);
    user.setProfileImageUrl(null);

    userRepository.save(user);
  }

  /**
   * Authenticates a user based on login credentials and returns a response DTO.
   *
   * @param loginRequest the DTO containing login credentials
   * @return an AuthResponseDTO containing the JWT token and user role
   * @throws AuthException if the email or password is incorrect
   */
  public AuthResponseDTO login(final LoginRequestDTO loginRequest) throws AuthException {

    final Optional<User> userOpt = userRepository.getUserByEmail(loginRequest.getEmail());
    if (userOpt.isEmpty()) {
      throw new AuthException("Email or Password is incorrect");
    }

    final User user = userOpt.get();

    if (!bcrypt.matches(loginRequest.getPassword(), user.getHashedPassword())) {
      throw new AuthException("Email or Password is incorrect");
    }

    final String token = jwtService.generateToken(user);

    final String role;
    if (user.getAccessLevel() == AccessLevel.ADMIN) {
      role = "ADMIN";
    } else if (user.getAccessLevel() == AccessLevel.FACULTY) {
      role = "FACULTY";
    } else {
      role = "USER";
    }

    return new AuthResponseDTO(token, role);
  }
}