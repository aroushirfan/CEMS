package com.cems.frontend.services;

import com.cems.shared.model.EventDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiEventService implements IEventService {

    private final HttpClient client = HttpClient.newHttpClient();

    // Added setPropertyNamingStrategy to match your Backend
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private final String API_URL = "http://localhost:8080/events";

    @Override
    public List<EventDto.EventResponseDTO> getAllEvents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .header("X-Requested-With", "XMLHttpRequest")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Because of the SNAKE_CASE strategy above, this will now correctly
            // map "date_time" from the JSON to "dateTime" in your Java object.
            return mapper.readValue(response.body(), new TypeReference<List<EventDto.EventResponseDTO>>() {});
        }
        else if (response.statusCode() == 204) {
            return List.of();
        }
        else {
            throw new RuntimeException("HTTP Error: " + response.statusCode() + " | Details: " + response.body());
        }
    }
}