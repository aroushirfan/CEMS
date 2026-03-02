package com.cems.frontend.services;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void login() throws Exception {
        AuthService authService = AuthService.getInstance();
        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"token\":\"fake-token\"}"));

        server.start(8080);

        String token = authService.login("email", "password");
        assertEquals("fake-token", token);

        server.shutdown();
    }

    @Test
    void signUp() throws Exception {
        AuthService authService = AuthService.getInstance();
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(201));
        server.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"error\":\"user already exists\"}"));

        server.start(8080);
        assertDoesNotThrow(() -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"));
        assertThrowsExactly(Exception.class, () -> authService.signUp("asdf", "asdf", "asdf", "asdf", "asdf"), "user already exists");
    }
}