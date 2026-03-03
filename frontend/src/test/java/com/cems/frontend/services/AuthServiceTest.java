package com.cems.frontend.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private MockWebServer server;
    private AuthService authService;

    @BeforeEach
    void beforeEach() throws IOException {
        server = new MockWebServer();
        server.start();
        int port = server.getPort();
        authService = AuthService.getInstance();
        authService.setPort(String.valueOf(port));
    }

    @AfterEach
    void afterEach() throws IOException {
        server.shutdown();
    }

    @Test
    void login() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"token\":\"fake-token\"}"));

        String token = authService.login("email", "password");
        assertEquals("fake-token", token);
    }

    @Test
    void signUp() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(201));
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"error\":\"user already exists\"}"));
        assertDoesNotThrow(() -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"));
        assertThrowsExactly(Exception.class, () -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"), "user already exists");
    }
}