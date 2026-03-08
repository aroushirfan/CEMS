package com.cems.frontend.services;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.utils.AttendanceMapper;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public List<Attendance> getEventAttendance(String eventId) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildGetRequest("attendance/event/" + eventId,AuthService.getInstance().getToken());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<AttendanceResponseDTO> attendanceResponseDTOS = objectMapper.readValue(response.body(),  new TypeReference<>() {});
            return AttendanceMapper.toModelList(attendanceResponseDTOS);
        }else {
            throw new RuntimeException("Fetch request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Returns attendance list
     * @return List<Attendance>
     */
    public String checkInEvent(UUID eventId) throws Exception {
        String requestUrl = String.format("attendance/event/%s/check-in", eventId.toString());
        HttpRequest request = LocalHttpClientHelper.buildPostRequest(requestUrl,AuthService.getInstance().getToken());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        }else {
            throw new RuntimeException("Fetch request failed with status code: " + response.statusCode());
        }
    }
}
