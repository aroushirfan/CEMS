package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RsvpServiceTest {
    private MockWebServer mockWebServer;
    private RsvpService rsvpService;
    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Syncing with your teammate's style: pointing the helper to the mock server port
        LocalHttpClientHelper.setPort(String.valueOf(mockWebServer.getPort()));
        HttpClient client = LocalHttpClientHelper.getClient();
        ObjectMapper mapper = LocalHttpClientHelper.getMapper();

        rsvpService = new RsvpService(client, mapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
    @Test
    void checkRsvp() throws Exception{
        UUID eventId = UUID.randomUUID();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("true"));

        String result = rsvpService.checkRsvp(eventId);
        assertEquals("true", result);
    }

    @Test
    void getRegisteredEvents() throws Exception{
        // Mocking a JSON list response for events
        String jsonResponse = "[{\"id\":\"" + UUID.randomUUID() + "\", \"title\":\"Test Event\"}]";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        List<Event> result = rsvpService.getRegisteredEvents();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void checkUserRsvp() throws Exception{
        UUID eventId = UUID.randomUUID();
        // service expects a JSON object with a "registered" boolean field
        String jsonResponse = "{\"registered\": true}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        boolean result = rsvpService.checkUserRsvp(eventId);
        assertTrue(result);
    }

    @Test
    void register() throws Exception{
        UUID eventId = UUID.randomUUID();
        // Your service expects a JSON object with a "message" field on 201 Created
        String jsonResponse = "{\"message\": \"Successfully registered\"}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setBody(jsonResponse));

        String result = rsvpService.register(eventId);
        assertEquals("Successfully registered", result);
    }

    @Test
    void cancelRegistration() throws Exception{
        UUID eventId = UUID.randomUUID();
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(204));

        String result = rsvpService.cancelRegistration(eventId);
        assertEquals("Registration cancelled successfully", result);
    }
}