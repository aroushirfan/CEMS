//package com.cems.cemsbackend.service;
//
//import com.cems.cemsbackend.model.AccessLevel;
//import com.cems.cemsbackend.model.User;
//import com.cems.cemsbackend.repository.UserRepository;
//import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
//import com.cems.shared.model.AuthDTO.AuthResponseDTO;
//import com.cems.shared.model.AuthDTO.LoginRequestDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//@Service
//public class AuthService {
//
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
//
//    public AuthService(JwtService jwtService, UserRepository userRepository) {
//        this.jwtService = jwtService;
//        this.userRepository = userRepository;
//    }
//
//    public static class AuthException extends Exception {
//        /**
//         * Constructs a new exception with the specified detail message.  The
//         * cause is not initialized, and may subsequently be initialized by
//         * a call to {@link #initCause}.
//         *
//         * @param message the detail message. The detail message is saved for
//         *                later retrieval by the {@link #getMessage()} method.
//         */
//        public AuthException(String message) {
//            super(message);
//        }
//    }
//
//    public void register(RegisterRequestDTO registerRequest) throws AuthException {
//        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
//            throw new AuthException("User already exists.");
//        }
//        String hashedPassword = bcrypt.encode(registerRequest.getPassword());
//        User user = new User(
//                registerRequest.getEmail(),
//                hashedPassword,
//                AccessLevel.USER,
//                registerRequest.getFirstName(),
//                registerRequest.getMiddleName(),
//                registerRequest.getLastName()
//        );
//        userRepository.save(user);
//    }
//
//    public AuthResponseDTO login(LoginRequestDTO loginRequest) throws AuthException {
//        var userOpt = userRepository.getUserByEmail(loginRequest.getEmail());
//        if (userOpt.isEmpty()) {
//            throw new AuthException("Email or Password is incorrect");
//        }
//        User user = userOpt.get();
//        if (!bcrypt.matches(loginRequest.getPassword(), user.getHashedPassword())) {
//            throw new AuthException("Email or Password is incorrect");
//        }
//        String token = jwtService.generateToken(user);
//        String role;
//        if (user.getAccessLevel() == AccessLevel.ADMIN) {
//            role = "ADMIN";
//        } else if (user.getAccessLevel() == AccessLevel.FACULTY) {
//            role = "FACULTY";
//        } else {
//            role = "USER";
//        }
//        return new AuthResponseDTO(token, role);
//    }
//}
package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        public AuthException(String message) {
            super(message);
        }
    }

    // ⭐ REGISTER USER
    public void register(RegisterRequestDTO registerRequest) throws AuthException {

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AuthException("User already exists.");
        }

        String hashedPassword = bcrypt.encode(registerRequest.getPassword());

        // ⭐ Use setters instead of old constructor
        User user = new User();
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

    // ⭐ LOGIN USER
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

        String role;
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
