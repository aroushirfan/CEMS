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
    void loginSuccess_WithFacultyUser_ReturnsFacultyRole() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("faculty@mail.com", "pass");

        User user = new User();
        user.setEmail("faculty@mail.com");
        user.setHashedPassword(encoder.encode("pass"));
        user.setAccessLevel(AccessLevel.FACULTY);

        when(userRepository.getUserByEmail("faculty@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt456");

        AuthResponseDTO response = authService.login(request);

        assertEquals("jwt456", response.getToken());
        assertEquals("FACULTY", response.getRole());
    }

    @Test
    void loginSuccess_WithRegularUser_ReturnsUserRole() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user@mail.com", "pass");

        User user = new User();
        user.setEmail("user@mail.com");
        user.setHashedPassword(encoder.encode("pass"));
        user.setAccessLevel(AccessLevel.USER);

        when(userRepository.getUserByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt789");

        AuthResponseDTO response = authService.login(request);

        assertEquals("jwt789", response.getToken());
        assertEquals("USER", response.getRole());
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
    void registerSuccess_CreatesNewUser() throws AuthService.AuthException {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "John", "M", "Doe",
                "john@mail.com",
                "pass", "pass"
        );

        when(userRepository.existsUserByEmail("john@mail.com")).thenReturn(false);

        assertDoesNotThrow(() -> authService.register(request));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerSuccess_SetsDefaultAccessLevel() throws AuthService.AuthException {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Jane", null, "Smith",
                "jane@mail.com",
                "pass", "pass"
        );

        when(userRepository.existsUserByEmail("jane@mail.com")).thenReturn(false);

        authService.register(request);

        // Capture the user object that was saved
        verify(userRepository).save(argThat(user ->
                user.getAccessLevel() == AccessLevel.USER &&
                user.getEmail().equals("jane@mail.com") &&
                user.getFirstName().equals("Jane") &&
                user.getLastName().equals("Smith")
        ));
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
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_EncryptsPassword() throws AuthService.AuthException {
        RegisterRequestDTO request = new RegisterRequestDTO(
                "Bob", null, "Builder",
                "bob@mail.com",
                "plainPassword", "plainPassword"
        );

        when(userRepository.existsUserByEmail("bob@mail.com")).thenReturn(false);

        authService.register(request);

        verify(userRepository).save(argThat(user ->
                user.getHashedPassword() != null &&
                !user.getHashedPassword().equals("plainPassword")
        ));
    }
}