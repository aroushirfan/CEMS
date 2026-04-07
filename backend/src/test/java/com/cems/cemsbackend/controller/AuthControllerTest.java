package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.service.AuthService;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import com.cems.shared.model.AuthDTO.RegisterRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ReturnsCreated() throws Exception {
        RegisterRequestDTO req = new RegisterRequestDTO(
                "Aroush", null, "Irfan",
                "test@mail.com",
                "pass", "pass"
        );

        ResponseEntity<?> response = authController.register(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authService).register(req);
    }

    @Test
    void register_ThrowsBadRequest() throws Exception {
        RegisterRequestDTO req = new RegisterRequestDTO(
                "Aroush", null, "Irfan",
                "test@mail.com",
                "pass", "pass"
        );

        doThrow(new AuthService.AuthException("exists")).when(authService).register(req);

        assertThrows(ResponseStatusException.class, () -> authController.register(req));
    }

    @Test
    void login_ReturnsResponse() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO("test@mail.com", "pass");
        AuthResponseDTO expected = new AuthResponseDTO("token123", "ADMIN");

        when(authService.login(req)).thenReturn(expected);

        AuthResponseDTO result = authController.login(req);

        assertEquals("token123", result.getToken());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void login_ThrowsBadRequest() throws Exception {
        LoginRequestDTO req = new LoginRequestDTO("test@mail.com", "wrong");

        when(authService.login(req)).thenThrow(new AuthService.AuthException("bad"));

        assertThrows(ResponseStatusException.class, () -> authController.login(req));
    }
}
