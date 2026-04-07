package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.AccessLevel;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void loginSuccess_ReturnsTokenAndRole() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("test@mail.com", "pass");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setHashedPassword(encoder.encode("pass"));
        user.setAccessLevel(AccessLevel.ADMIN);

        when(userRepository.getUserByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt123");

        AuthResponseDTO response = authService.login(request);

        assertEquals("jwt123", response.getToken());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void loginFails_WhenEmailNotFound() {
        LoginRequestDTO request = new LoginRequestDTO("missing@mail.com", "pass");

        when(userRepository.getUserByEmail("missing@mail.com")).thenReturn(Optional.empty());

        assertThrows(AuthService.AuthException.class, () -> authService.login(request));
    }

    @Test
    void loginFails_WhenPasswordIncorrect() {
        LoginRequestDTO request = new LoginRequestDTO("test@mail.com", "wrong");

        User user = new User();
        user.setEmail("test@mail.com");
        user.setHashedPassword(encoder.encode("correct"));

        when(userRepository.getUserByEmail("test@mail.com")).thenReturn(Optional.of(user));

        assertThrows(AuthService.AuthException.class, () -> authService.login(request));
    }

    @Test
    void registerFails_WhenEmailAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Aroush", null, "Irfan",
                "test@mail.com",
                "pass", "pass"
        );

        when(userRepository.existsUserByEmail("test@mail.com")).thenReturn(true);

        assertThrows(AuthService.AuthException.class, () -> authService.register(request));
    }
}