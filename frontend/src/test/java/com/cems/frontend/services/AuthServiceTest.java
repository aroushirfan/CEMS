package com.cems.frontend.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final int PORT = Integer.parseInt(System.getenv("PORT"));
    private MockWebServer server;

    @BeforeEach
    void beforeEach() {
        server = new MockWebServer();
    }

    @AfterEach
    void afterEach() {
        if (server != null) {
            try {
                server.shutdown();
            } catch (IOException ignored) {}
        }
    }

    @Test
    void login() throws Exception {
        AuthService authService = AuthService.getInstance();

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"token\":\"fake-token\"}"));

        server.start(PORT);

        String token = authService.login("email", "password");
        assertEquals("fake-token", token);
    }

    @Test
    void signUp() throws Exception {
        AuthService authService = AuthService.getInstance();
        server.enqueue(new MockResponse()
                .setResponseCode(201));
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"error\":\"user already exists\"}"));

        server.start(PORT);
        assertDoesNotThrow(() -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"));
        assertThrowsExactly(Exception.class, () -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"), "user already exists");
    }
}