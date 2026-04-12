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

public class AttendanceService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AttendanceService(HttpClient client, ObjectMapper mapper) {
        this.httpClient = client;
        this.objectMapper = mapper;
    }

    /**
     * Returns attendance list
     * @return List<Attendance>
     */
    public List<Attendance> getEventAttendance(String eventId) throws IOException, InterruptedException {
        HttpRequest request = LocalHttpClientHelper.buildRequest("attendance/event/" + eventId).authorization(AuthService.getInstance().getToken()).get();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            List<AttendanceResponseDTO> attendanceResponseDTOS = objectMapper.readValue(response.body(),  new TypeReference<>() {});
            return AttendanceMapper.toModelList(attendanceResponseDTOS);
        }else {
            throw new IOException("Fetch request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Returns attendance list
     * @return List<Attendance>
     */
    public String checkInEvent(UUID eventId) throws IOException, InterruptedException {
        String requestUrl = String.format("attendance/event/%s/check-in", eventId.toString());
        HttpRequest request = LocalHttpClientHelper.buildRequest(requestUrl).authorization(AuthService.getInstance().getToken()).post(null);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code || response.statusCode() == HttpStatus.CREATED.code) {
            return objectMapper.readTree(response.body()).get("message").asText();
        } else {
            throw new IOException(objectMapper.readTree(response.body()).get("error").asText());
        }
    }

    /**
     * Returns True or false if a user has marked attendance
     * @return boolean
     */
    public boolean hasCheckedIn(UUID eventId) throws IOException, InterruptedException {
        String requestUrl = String.format("attendance/event/%s/checked-in", eventId.toString());
        HttpRequest request = LocalHttpClientHelper.buildRequest(requestUrl).authorization(AuthService.getInstance().getToken()).get();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            return objectMapper.readTree(response.body()).get("checkedIn").asBoolean();
        }else {
            throw new IOException("Fetch request failed with status code: " + response.statusCode());
        }
    }
}
