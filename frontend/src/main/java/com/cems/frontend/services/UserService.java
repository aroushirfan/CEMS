package com.cems.frontend.services;

import com.cems.frontend.models.User;
import com.cems.frontend.models.HttpClientObject;
import com.cems.frontend.utils.UserMapper;
import com.cems.shared.model.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserService {

    private final HttpClient client;
    private final AuthService authService = AuthService.getInstance();

    private final ObjectMapper mapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private final String API_URL = String.format("%s/users", System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8080"));

    public UserService() {
        this.client = HttpClientObject.getClient();
    }

    // GET all users
    public List<User> getAllUsers() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + authService.getToken())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<UserDTO> dtos = mapper.readValue(response.body(), new TypeReference<List<UserDTO>>() {});
            return UserMapper.toModelList(dtos);
        } else if (response.statusCode() == 204) {
            return List.of();
        } else {
            throw new RuntimeException("Fetch users failed: " + response.statusCode());
        }
    }

    // GET single user
    public User getUserById(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + authService.getToken())
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            UserDTO dto = mapper.readValue(response.body(), UserDTO.class);
            return UserMapper.toModel(dto);
        } else {
            throw new RuntimeException("User not found: " + id);
        }
    }

    // UPDATE access level
    public void updateAccessLevel(String id, int newLevel) throws Exception {
        UserDTO dto = new UserDTO();
        dto.setAccessLevel(newLevel);

        String json = mapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id + "/access-level"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authService.getToken())
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Role update failed: " + response.body());
        }
    }
}
