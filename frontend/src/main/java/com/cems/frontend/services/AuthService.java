package com.cems.frontend.services;

import com.cems.frontend.models.HttpClientObject;
import com.cems.frontend.models.ResponseError;
import com.cems.frontend.utils.LocalStorage;
import com.cems.shared.model.AuthDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {
    private final HttpClient client;

    private static AuthService instance = null;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    private String port = System.getenv().getOrDefault("PORT","8081");

    private String API_URL = String.format("http://localhost:%s/auth", port);

    private AuthService() {
        this.client = HttpClientObject.getClient();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    // String firstName, String middleName, String lastName, String email, String password, String repeatPassword

    public void signUp(String firstName, String lastName, String email, String password, String confirmPassword) throws Exception {
        AuthDTO.RegisterRequestDTO registerRequestDTO = new AuthDTO.RegisterRequestDTO(firstName, null, lastName, email, password, confirmPassword);

        String requestBody = mapper.writeValueAsString(registerRequestDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 201) {
            ResponseError responseError = mapper.readValue(httpResponse.body(), ResponseError.class);
            throw new Exception(responseError.getError());
        }
    }

    /**
     * Returns token
     * @param email
     * @param password
     * @return token
     * @throws Exception
     */
    public AuthDTO.AuthResponseDTO login(String email, String password) throws Exception {
        AuthDTO.LoginRequestDTO loginRequestDTO = new AuthDTO.LoginRequestDTO(email, password);

        String requestBody = mapper.writeValueAsString(loginRequestDTO);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            AuthDTO.AuthResponseDTO authResponseDTO = mapper.readValue(httpResponse.body(), AuthDTO.AuthResponseDTO.class);
            LocalStorage.set("token", authResponseDTO.getToken());
            LocalStorage.set("role", authResponseDTO.getRole());
            System.out.println(authResponseDTO.getToken());
            return authResponseDTO;
        } else {
            return null;
        }
    }

    public String getToken() {
        String token = LocalStorage.get("token");
        if (token == null ||token.isEmpty()) {
            return "";
        } else {
            return token;
        }
    }

    public void logout() {
        LocalStorage.remove("token");
        LocalStorage.remove("role");
    }

    public void setPort(String port) {
        this.port = port;
        API_URL = String.format("http://localhost:%s/auth", port);
    }
}
