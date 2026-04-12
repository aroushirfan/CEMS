package com.cems.frontend.services;

import com.cems.frontend.models.ResponseError;
import com.cems.frontend.utils.HttpStatus;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.utils.LocalStorage;
import com.cems.shared.model.AuthDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class AuthService {
    private final HttpClient client;

    private static AuthService instance = null;
    private static final String TOKEN = "token";

    private final ObjectMapper mapper = LocalHttpClientHelper.getMapper();

    private AuthService() {
        this.client = LocalHttpClientHelper.getClient();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }


    public void signUp(String firstName, String lastName, String email, String password, String confirmPassword) throws Exception {
        AuthDTO.RegisterRequestDTO registerRequestDTO = new AuthDTO.RegisterRequestDTO(firstName, null, lastName, email, password, confirmPassword);

        String requestBody = mapper.writeValueAsString(registerRequestDTO);

        HttpRequest request = LocalHttpClientHelper.buildRequest("auth/register").post(requestBody);

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != HttpStatus.CREATED.code) {
            ResponseError responseError = mapper.readValue(httpResponse.body(), ResponseError.class);
            throw new IOException(responseError.getError());
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

        HttpRequest request = LocalHttpClientHelper.buildRequest("auth/login").post(requestBody);

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        AuthDTO.AuthResponseDTO authResponseDTO = null;
        if (httpResponse.statusCode() == HttpStatus.OK.code) {
            authResponseDTO = mapper.readValue(httpResponse.body(), AuthDTO.AuthResponseDTO.class);
            LocalStorage.set(TOKEN, authResponseDTO.getToken());
            LocalStorage.set("role", authResponseDTO.getRole());
        }

        return authResponseDTO;

    }

    public String getToken() {
        String token = LocalStorage.get(TOKEN);
        if (token == null ||token.isEmpty()) {
            return "";
        } else {
            return token;
        }
    }

    public void logout() {
        LocalStorage.remove(TOKEN);
        LocalStorage.remove("role");
    }
}