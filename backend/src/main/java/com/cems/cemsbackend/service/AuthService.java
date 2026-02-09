package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public AuthService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public static class AuthException extends Exception {
        /**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public AuthException(String message) {
            super(message);
        }
    }

    public void register(RegisterRequestDTO registerRequest) throws AuthException {
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AuthException("User already exists.");
        }
        String hashedPassword = bcrypt.encode(registerRequest.getPassword());
        User user = new User(
                registerRequest.getEmail(),
                hashedPassword,
                AccessLevel.USER,
                registerRequest.getFirstName(),
                registerRequest.getMiddleName(),
                registerRequest.getLastName()
        );
        userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequest) throws AuthException {
        var userOpt = userRepository.getUserByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            throw new AuthException("Email or Password is incorrect");
        }
        User user = userOpt.get();
        if (!bcrypt.matches(loginRequest.getPassword(), user.getHashedPassword())) {
            throw new AuthException("Email or Password is incorrect");
        }
        String token = jwtService.generateToken(user);
        return new AuthResponseDTO(token);
    }
}
