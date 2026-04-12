package com.cems.frontend.services;

import com.cems.frontend.models.User;
import com.cems.frontend.utils.HttpStatus;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.utils.UserMapper;
import com.cems.shared.model.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserService {

    private final HttpClient client;
    private final AuthService authService = AuthService.getInstance();

    private final ObjectMapper mapper = LocalHttpClientHelper.getMapper();

    private static final String BASE_URL = "users";

    public UserService() {
        this.client = LocalHttpClientHelper.getClient();
    }

    //  GET /users/me
    public User getCurrentUser() throws IOException, InterruptedException {
        HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me").authorization(authService.getToken()).get();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            UserDTO dto = mapper.readValue(response.body(), UserDTO.class);
            return UserMapper.toModel(dto);
        } else {
            throw new IOException("Failed to load current user: " + response.statusCode());
        }
    }

    //  PUT /users/me
    public User updateCurrentUser(UserDTO dto) throws Exception {
        dto.setEmail(null);
        dto.setAccessLevel(0);
        String json = mapper.writeValueAsString(dto);

        HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me").authorization(authService.getToken()).put(json);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            UserDTO updatedDto = mapper.readValue(response.body(), UserDTO.class);
            return UserMapper.toModel(updatedDto);
        } else {
            throw new IOException("Failed to update user: " + response.body());
        }
    }

    // GET all users
    public List<User> getAllUsers() throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL).authorization(authService.getToken()).get();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            List<UserDTO> dtos = mapper.readValue(response.body(), new TypeReference<>() {});
            return UserMapper.toModelList(dtos);
        } else if (response.statusCode() == HttpStatus.NO_CONTENT.code) {
            return List.of();
        } else {
            throw new IOException("Fetch users failed: " + response.statusCode());
        }
    }

    // GET single user
    public User getUserById(String id) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest("users/" + id).authorization(authService.getToken()).get();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            UserDTO dto = mapper.readValue(response.body(), UserDTO.class);
            return UserMapper.toModel(dto);
        } else {
            throw new IOException("User not found: " + id);
        }
    }

    // UPDATE access level (ADMIN)
    public void updateAccessLevel(String id, int newLevel) throws Exception {
        UserDTO dto = new UserDTO();
        dto.setAccessLevel(newLevel);

        String json = mapper.writeValueAsString(dto);

        HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/" + id + "/access-level").authorization(authService.getToken()).put(json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != HttpStatus.OK.code) {
            throw new IOException("Role update failed: " + response.body());
        }
    }
    //  DELETE /users/me
    public void deleteCurrentUser() throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me").authorization(authService.getToken()).delete();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != HttpStatus.OK.code) {
            throw new IOException("Failed to delete user: " + response.body());
        }
    }

}
