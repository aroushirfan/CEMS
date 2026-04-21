package com.cems.frontend.services;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceServiceTest {
  private MockWebServer mockWebServer;
  private AttendanceService attendanceService;
  private final UUID testUuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

  @BeforeEach
  void setUp() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();

    LocalHttpClientHelper.setPort(String.valueOf(mockWebServer.getPort()));
    HttpClient client = LocalHttpClientHelper.getClient();
    ObjectMapper mapper = LocalHttpClientHelper.getMapper();

    attendanceService = new AttendanceService(client, mapper);
  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void testGetEventAttendance_success() throws Exception {
    String jsonResponse = "[{\"event_id\":\"" + testUuid + "\",\"user_id\":\"" + testUuid + "\",\"first_name\":\"tom\",\"last_name\":\"cruise\",\"email\":\"example@metropolia.fi\",\"status\":\"PRESENT\",\"check_in_time\":\"2026-03-03T09:45:00Z\"}]";
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(jsonResponse));

    List<Attendance> result = attendanceService.getEventAttendance(testUuid.toString());
    assertFalse(result.isEmpty());
    assertEquals("tom cruise", result.getFirst().getName());
  }

  @Test
  void testGetEventAttendance_fail() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    assertThrows(IOException.class, () -> attendanceService.getEventAttendance(testUuid.toString()));
  }

  @Test
  void testCheckInEvent_success() throws Exception {
    // Covers lines 80-82 (Status 201 and "message" extraction)
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(201)
        .setBody("{\"message\": \"Check-in successful\"}"));

    String result = attendanceService.checkInEvent(testUuid);
    assertEquals("Check-in successful", result);
  }

  @Test
  void testCheckInEvent_fail() {
    // Covers line 84 (Extracting "error" from body)
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(400)
        .setBody("{\"error\": \"Already checked in\"}"));

    IOException ex = assertThrows(IOException.class, () -> attendanceService.checkInEvent(testUuid));
    assertEquals("Already checked in", ex.getMessage());
  }

  @Test
  void testHasCheckedIn_success() throws Exception {
    // Covers line 104-105 (Boolean parsing)
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("{\"checkedIn\": true}"));

    assertTrue(attendanceService.hasCheckedIn(testUuid));
  }

  @Test
  void testHasCheckedIn_fail() {
    // Covers line 107 (Fetch failure)
    mockWebServer.enqueue(new MockResponse().setResponseCode(404));
    assertThrows(IOException.class, () -> attendanceService.hasCheckedIn(testUuid));
  }
}