package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.EventMapper; // Assuming you put the mapper in .utils
import com.cems.shared.model.EventDto;
import com.cems.shared.model.EventDto.EventResponseDTO;
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
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private final String API_URL = "http://localhost:8080/events";

    @Override
    public List<Event> getAllEvents() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<EventResponseDTO> dtos = mapper.readValue(response.body(), new TypeReference<List<EventResponseDTO>>() {});
            // Convert DTOs to Models using the Mapper
            return EventMapper.toModelList(dtos);
        } else if (response.statusCode() == 204) {
            return List.of();
        } else {
            throw new RuntimeException("Fetch failed: " + response.statusCode());
        }
    }

    @Override
    public Event createEvent(EventDto.EventRequestDTO data) throws Exception {
        String json = mapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
            return EventMapper.toModel(dto); // Return model
        } else {
            throw new RuntimeException("Creation failed: " + response.body());
        }
    }

    @Override
    public Event updateEvent(String id, EventDto.EventRequestDTO data) throws Exception {
        String json = mapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
            return EventMapper.toModel(dto); // Return model
        } else {
            throw new RuntimeException("Update failed: " + response.body());
        }
    }

    @Override
    public void deleteEvent(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204 && response.statusCode() != 200) {
            throw new RuntimeException("Delete failed: " + response.body());
        }
    }

    @Override
    public Event getEventById(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
            return EventMapper.toModel(dto);
        } else {
            throw new RuntimeException("Event not found: " + id);
        }
    }
}
