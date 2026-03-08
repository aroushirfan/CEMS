package com.cems.frontend.services;

import com.cems.frontend.models.Attendance;
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

class AttendanceServiceTest {
    private MockWebServer mockWebServer;
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        LocalHttpClientHelper.setPort(String.valueOf(mockWebServer.getPort()));
        HttpClient client = LocalHttpClientHelper.getClient();
        ObjectMapper mapper = LocalHttpClientHelper.getMapper();

        attendanceService = new AttendanceService(
                client,
                mapper
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testGetEventAttendance_success() throws Exception {
        String eventId = "550e8400-e29b-41d4-a716-446655440000";
        String jsonResponse = """
                [
                    {
                        "event_id": "550e8400-e29b-41d4-a716-446655440000",
                        "user_id": "550e8400-e29b-41d4-a716-446655440000",
                        "first_name": "tom",
                        "last_name": "cruise",
                        "email": "example@metropolia.fi",
                        "status": "PRESENT",
                        "check_in_time": "2026-03-03T09:45:00Z"
                    },
                     {
                        "event_id": "550e8400-e29b-41d4-a716-446655440000",
                        "user_id": "550e8400-e29b-41d4-a716-446655441000",
                        "first_name": "kevin",
                        "last_name": "hart",
                        "email": "example2@metropolia.fi",
                        "status": "ABSENT",
                        "check_in_time": "2026-03-03T09:45:00Z"
                    }
                ]
                """;

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(jsonResponse)
                        .addHeader("Content-Type", "application/json")
        );

        List<Attendance> result = attendanceService.getEventAttendance(eventId);

        assertEquals(2, result.size());
        assertEquals(UUID.fromString(eventId), result.getFirst().getEventId());
        assertEquals("tom cruise", result.getFirst().getName());
        assertEquals("example@metropolia.fi", result.getFirst().getEmail());
        assertEquals("PRESENT", result.getFirst().getStatus());

        assertEquals(UUID.fromString(eventId), result.get(1).getEventId());
        assertEquals("kevin hart", result.get(1).getName());
        assertEquals("example2@metropolia.fi", result.get(1).getEmail());
        assertEquals("ABSENT", result.get(1).getStatus());
    }

    @Test
    void testGetEventAttendance_403_throwsException() {
        String eventId = "550e8400-e29b-41d4-a716-446655440000";

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(403)
        );

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                attendanceService.getEventAttendance(eventId)
        );

        assertEquals("Fetch request failed with status code: 403", ex.getMessage());
    }

}