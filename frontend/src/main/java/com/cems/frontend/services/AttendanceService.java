package com.cems.frontend.services;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.utils.AttendanceMapper;
import com.cems.frontend.utils.HttpStatus;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

/**
 * Service for attendance-related API operations.
 *
 * <p>This service communicates with attendance endpoints and maps DTO responses
 * into frontend attendance models.</p>
 */
public class AttendanceService {
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  /**
   * Creates an attendance service with injected HTTP and JSON dependencies.
   *
   * @param client HTTP client used to execute requests
   * @param mapper Jackson object mapper used for payload serialization and parsing
   */
  public AttendanceService(HttpClient client, ObjectMapper mapper) {
    this.httpClient = client;
    this.objectMapper = mapper;
  }

  /**
   * Fetches all attendance entries for a specific event.
   *
   * @param eventId event identifier
   * @return attendance records for the event
   * @throws IOException          if the backend responds with a non-OK status or parsing fails
   * @throws InterruptedException if the request thread is interrupted
   */
  public List<Attendance> getEventAttendance(String eventId) throws IOException,
      InterruptedException {
    HttpRequest request = LocalHttpClientHelper.buildRequest("attendance/event/" + eventId)
        .authorization(AuthService.getInstance().getToken())
        .get();
    HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      List<AttendanceResponseDTO> attendanceResponseDtos = objectMapper.readValue(response.body(),
          new TypeReference<>() {
          });
      return AttendanceMapper.toModelList(attendanceResponseDtos);
    } else {
      throw new IOException("Fetch request failed with status code: " + response.statusCode());
    }
  }

  /**
   * Marks the current authenticated user as checked in for an event.
   *
   * @param eventId event identifier
   * @return success message returned by the backend
   * @throws IOException          if check-in fails or the backend returns an error payload
   * @throws InterruptedException if the request thread is interrupted
   */
  public String checkInEvent(UUID eventId) throws IOException, InterruptedException {
    String requestUrl = String.format("attendance/event/%s/check-in", eventId.toString());
    HttpRequest request = LocalHttpClientHelper.buildRequest(requestUrl)
        .authorization(AuthService.getInstance().getToken())
        .post(null);
    HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code || response.statusCode()
        == HttpStatus.CREATED.code) {
      return objectMapper.readTree(response.body()).get("message").asText();
    } else {
      throw new IOException(objectMapper.readTree(response.body()).get("error").asText());
    }
  }

  /**
   * Checks whether the current authenticated user has already checked in to an event.
   *
   * @param eventId event identifier
   * @return {@code true} if the user has checked in, otherwise {@code false}
   * @throws IOException          if the backend responds with a non-OK status or parsing fails
   * @throws InterruptedException if the request thread is interrupted
   */
  public boolean hasCheckedIn(UUID eventId) throws IOException, InterruptedException {
    String requestUrl = String.format("attendance/event/%s/checked-in", eventId.toString());
    HttpRequest request = LocalHttpClientHelper.buildRequest(requestUrl)
        .authorization(AuthService.getInstance().getToken())
        .get();
    HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      return objectMapper.readTree(response.body()).get("checkedIn").asBoolean();
    } else {
      throw new IOException("Fetch request failed with status code: " + response.statusCode());
    }
  }
}
