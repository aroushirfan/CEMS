package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.EventMapper;
import com.cems.frontend.utils.HttpStatus;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.shared.model.EventDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

/**
 * Service for RSVP-related event operations.
 *
 * <p>This service handles RSVP checks, registration state, and retrieval of
 * events registered by the authenticated user.</p>
 */
public class RsvpService {
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;

  /**
   * Creates an RSVP service with injected HTTP and JSON dependencies.
   *
   * @param client HTTP client used to execute requests
   * @param mapper Jackson object mapper used to parse response payloads
   */
  public RsvpService(HttpClient client, ObjectMapper mapper) {
    this.httpClient = client;
    this.objectMapper = mapper;
  }

  /**
   * Retrieves RSVP status payload for the given event.
   *
   * @param eventId event identifier
   * @return backend response body describing RSVP status
   * @throws IOException if transport, parsing, or non-success response handling fails
   */
  public String checkRsvp(UUID eventId) throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString())
        .authorization(AuthService.getInstance().getToken())
        .get();
    final HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      return response.body();
    } else {
      throw new IOException("Post request failed with status code: " + response.statusCode());
    }
  }

  /**
   * Fetches events the authenticated user has registered for.
   *
   * @return list of registered events
   * @throws IOException if transport, parsing, or non-success response handling fails
   */
  public List<Event> getRegisteredEvents() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/my-events")
        .authorization(AuthService.getInstance().getToken()).get();
    final HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final List<EventDto.EventResponseDTO> eventDtos = objectMapper.readValue(response.body(),
          new TypeReference<>() {
          });
      return EventMapper.toModelList(eventDtos);
    } else {
      throw new IOException("Fetch request failed with status code: " + response.statusCode());
    }
  }

  /**
   * Checks whether the authenticated user is registered for an event.
   *
   * @param eventId event identifier
   * @return {@code true} when registered, otherwise {@code false}
   * @throws IOException if transport, parsing, or non-success response handling fails
   */
  public boolean checkUserRsvp(UUID eventId) throws IOException, InterruptedException {
    final String url = String.format("rsvp/%s/registered", eventId);
    final HttpRequest request = LocalHttpClientHelper.buildRequest(url)
            .authorization(AuthService.getInstance().getToken()).get();
    final HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      return objectMapper.readTree(response.body()).get("registered").asBoolean();
    } else {
      throw new IOException("Fetch request failed with status code: "
              + response.statusCode() + "error: " + response.body());
    }
  }

  /**
   * Registers the authenticated user for an event.
   *
   * @param eventId event identifier
   * @return success message returned by the backend
   * @throws IOException if transport, parsing, or non-success response handling fails
   */
  public String register(UUID eventId) throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString())
        .authorization(AuthService.getInstance().getToken()).post(null);
    final HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == HttpStatus.CREATED.code) {
      return objectMapper.readTree(response.body()).get("message").asText();
    } else {
      throw new IOException(objectMapper.readTree(response.body()).get("message").asText());
    }
  }

  /**
   * Cancels the authenticated user's registration for an event.
   *
   * @param eventId event identifier
   * @return cancellation confirmation message
   * @throws IOException if transport or non-success response handling fails
   */
  public String cancelRegistration(UUID eventId) throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString())
        .authorization(AuthService.getInstance().getToken()).delete();
    final HttpResponse<String> response = httpClient.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == 204 || response.statusCode() == 200) {
      return "Registration cancelled successfully";
    } else {
      throw new IOException("Fetch request failed with status code: " + response.statusCode());
    }
  }
}
