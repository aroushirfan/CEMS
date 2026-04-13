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

/**
 * Service for user-related API operations.
 *
 * <p>This service provides methods for managing the current user profile,
 * querying users, and updating user access levels.</p>
 */
public class UserService {

  private final HttpClient client;
  private final AuthService authService = AuthService.getInstance();

  private final ObjectMapper mapper = LocalHttpClientHelper.getMapper();

  private static final String BASE_URL = "users";

  /**
   * Creates a user service using the shared local HTTP client.
   */
  public UserService() {
    this.client = LocalHttpClientHelper.getClient();
  }

  /**
   * Fetches the currently authenticated user.
   *
   * @return current user model
   * @throws IOException          if the backend response is not successful or parsing fails
   * @throws InterruptedException if the request thread is interrupted
   */
  public User getCurrentUser() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me")
        .authorization(authService.getToken()).get();
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final UserDTO dto = mapper.readValue(response.body(), UserDTO.class);
      return UserMapper.toModel(dto);
    } else {
      throw new IOException("Failed to load current user: " + response.statusCode());
    }
  }

  /**
   * Updates the currently authenticated user's profile.
   *
   * <p>Email and access level are not updated through this method and are reset
   * before sending the request.</p>
   *
   * @param dto user payload containing profile fields to update
   * @return updated user model
   * @throws IOException if serialization, transport, or backend validation fails
   */
  public User updateCurrentUser(UserDTO dto) throws IOException, InterruptedException {
    dto.setEmail(null);
    dto.setAccessLevel(0);
    final String json = mapper.writeValueAsString(dto);

    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me")
            .authorization(authService.getToken())
            .put(json);
    final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final UserDTO updatedDto = mapper.readValue(response.body(), UserDTO.class);
      return UserMapper.toModel(updatedDto);
    } else {
      throw new IOException("Failed to update user: " + response.body());
    }
  }

  /**
   * Fetches all users.
   *
   * @return list of users, or an empty list when no users are returned
   * @throws IOException if transport, parsing, or non-handled response handling fails
   */
  public List<User> getAllUsers() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL)
        .authorization(authService.getToken()).get();

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    final List<User> result;
    if (response.statusCode() == HttpStatus.OK.code) {
      final List<UserDTO> dtos = mapper.readValue(response.body(),
          new TypeReference<>() {
          });
      result = UserMapper.toModelList(dtos);
    } else if (response.statusCode() == HttpStatus.NO_CONTENT.code) {
      result = List.of();
    } else {
      throw new IOException("Fetch users failed: " + response.statusCode());
    }
    return result;
  }

  /**
   * Fetches a single user by identifier.
   *
   * @param id user identifier
   * @return user model for the given identifier
   * @throws IOException if transport fails, parsing fails, or user retrieval is unsuccessful
   */
  public User getUserById(String id) throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("users/" + id)
        .authorization(authService.getToken()).get();

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final UserDTO dto = mapper.readValue(response.body(), UserDTO.class);
      return UserMapper.toModel(dto);
    } else {
      throw new IOException("User not found: " + id);
    }
  }

  /**
   * Updates a user's access level.
   *
   * @param id       user identifier
   * @param newLevel new access level value
   * @throws IOException if serialization, transport, or backend update handling fails
   */
  public void updateAccessLevel(String id, int newLevel) throws IOException, InterruptedException {
    final UserDTO dto = new UserDTO();
    dto.setAccessLevel(newLevel);

    final String json = mapper.writeValueAsString(dto);

    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/" + id + "/access-level")
        .authorization(authService.getToken()).put(json);

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != HttpStatus.OK.code) {
      throw new IOException("Role update failed: " + response.body());
    }
  }

  /**
   * Deletes the currently authenticated user.
   *
   * @throws IOException if transport fails or backend deletion response is unsuccessful
   */
  public void deleteCurrentUser() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/me")
        .authorization(authService.getToken()).delete();
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != HttpStatus.OK.code) {
      throw new IOException("Failed to delete user: " + response.body());
    }
  }

}
