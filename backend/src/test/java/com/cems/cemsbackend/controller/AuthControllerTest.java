package com.cems.cemsbackend.controller;



import com.cems.cemsbackend.service.AuthService;
import com.cems.shared.model.AuthDTO.LoginRequestDTO;
import com.cems.shared.model.AuthDTO.AuthResponseDTO;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {


    @Test
    void loginShouldReturnToken() throws AuthService.AuthException {

        AuthService authService = Mockito.mock(AuthService.class);

        AuthController controller = new AuthController(authService);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("password");

        AuthResponseDTO response = new AuthResponseDTO("token123");

        Mockito.when(authService.login(request)).thenReturn(response);

        AuthResponseDTO result = controller.login(request);

        assertEquals("token123", result.getToken());
    }
}