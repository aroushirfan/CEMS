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

/**
 * Authentication service responsible for user registration, login, and local session state.
 *
 * <p>This service is implemented as a singleton and persists authentication data in
 * {@link LocalStorage}.</p>
 */
public final class AuthService {
  private final HttpClient client;

  private static AuthService instance = null;
  private static final String TOKEN = "token";

  private final ObjectMapper mapper = LocalHttpClientHelper.getMapper();

  private AuthService() {
    this.client = LocalHttpClientHelper.getClient();
  }

  /**
   * Returns the singleton instance of the authentication service.
   *
   * @return shared {@link AuthService} instance
   */
  public static AuthService getInstance() {
    if (instance == null) {
      instance = new AuthService();
    }
    return instance;
  }

  /**
   * Registers a new user account.
   *
   * @param firstName       user's first name
   * @param lastName        user's last name
   * @param email           user's email address
   * @param password        user's password
   * @param confirmPassword repeated password for confirmation
   * @throws IOException if request serialization, transport, or backend validation fails
   */
  public void signUp(String firstName, String lastName, String email,
                     String password, String confirmPassword) throws IOException,
      InterruptedException {
    AuthDTO.RegisterRequestDTO registerRequestDto = new AuthDTO.RegisterRequestDTO(firstName,
        null, lastName, email, password, confirmPassword);

    String requestBody = mapper.writeValueAsString(registerRequestDto);

    HttpRequest request = LocalHttpClientHelper.buildRequest("auth/register").post(requestBody);

    HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (httpResponse.statusCode() != HttpStatus.CREATED.code) {
      ResponseError responseError = mapper.readValue(httpResponse.body(), ResponseError.class);
      throw new IOException(responseError.getError());
    }
  }

  /**
   * Authenticates a user and stores token and role in local storage on success.
   *
   * @param email    user's email address
   * @param password user's password
   * @return authentication response, or {@code null} when credentials are rejected
   * @throws Exception if request serialization or HTTP transport fails
   */
  public AuthDTO.AuthResponseDTO login(String email, String password) throws IOException,
      InterruptedException {
    AuthDTO.LoginRequestDTO loginRequestDto = new AuthDTO.LoginRequestDTO(email, password);

    String requestBody = mapper.writeValueAsString(loginRequestDto);

    HttpRequest request = LocalHttpClientHelper.buildRequest("auth/login").post(requestBody);

    HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
    AuthDTO.AuthResponseDTO authResponseDto = null;
    if (httpResponse.statusCode() == HttpStatus.OK.code) {
      authResponseDto = mapper.readValue(httpResponse.body(), AuthDTO.AuthResponseDTO.class);
      LocalStorage.set(TOKEN, authResponseDto.getToken());
      LocalStorage.set("role", authResponseDto.getRole());
    }

    return authResponseDto;

  }

  /**
   * Returns the currently stored authentication token.
   *
   * @return token string, or empty string when no token is stored
   */
  public String getToken() {
    String token = LocalStorage.get(TOKEN);
    if (token == null || token.isEmpty()) {
      return "";
    } else {
      return token;
    }
  }

  /**
   * Clears locally stored authentication token and role.
   */
  public void logout() {
    LocalStorage.remove(TOKEN);
    LocalStorage.remove("role");
  }
}