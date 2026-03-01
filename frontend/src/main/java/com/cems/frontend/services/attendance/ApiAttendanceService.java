package com.cems.frontend.services.attendance;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.services.LocalHttpClient;
import com.cems.frontend.utils.AttendanceMapper;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiAttendanceService implements IAttendanceService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiAttendanceService(HttpClient client, ObjectMapper mapper) {
        this.httpClient = client;
        this.objectMapper = mapper;
    }

    @Override
    public List<Attendance> getEventAttendance() throws Exception {
        HttpRequest request = LocalHttpClient.buildGetRequest("attendance");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<AttendanceResponseDTO> attendanceResponseDTOS = objectMapper.readValue(response.body(), new TypeReference<List<AttendanceResponseDTO>>() {});
            return AttendanceMapper.toModelList(attendanceResponseDTOS);
        }else {
            throw new RuntimeException("Fetch request failed with status code: " + response.statusCode());
        }
    }
}
