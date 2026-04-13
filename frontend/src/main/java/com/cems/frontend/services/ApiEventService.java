package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.*;
import com.cems.shared.model.EventDto;
import com.cems.shared.model.EventDto.EventResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * HTTP-backed implementation of {@link IEventService} for event operations.
 *
 * <p>This service calls backend event endpoints, maps DTO payloads to frontend
 * models, and enforces status-code based error handling.</p>
 */
public class ApiEventService implements IEventService {

  private final HttpClient client;
  private final ObjectMapper mapper = LocalHttpClientHelper.getMapper();
  private final AuthService authService = AuthService.getInstance();
  private static final String BASE_URL = "events";

  /**
   * Creates an event API service using the shared local HTTP client.
   */
  public ApiEventService() {
    this.client = LocalHttpClientHelper.getClient();
  }

  /**
   * Fetches all events from the backend.
   *
   * @return list of events, or an empty list when no events exist
   * @throws IOException          if the request fails or returns an unexpected status
   * @throws InterruptedException if the request thread is interrupted
   */
  @Override
  public List<Event> getAllEvents() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest(
        BASE_URL+ "/all/" + LocaleUtil.getInstance().getLocale().getLanguage())
        .get();
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    final List<Event> result;

    if (response.statusCode() == HttpStatus.OK.code) {
      final List<EventResponseDTO> eventDtos = mapper.readValue(response.body(),
          new TypeReference<>() {
          });
      result = EventMapper.toModelList(eventDtos); // Convert DTOs to Models using the Mapper
    } else if (response.statusCode() == HttpStatus.NO_CONTENT.code) {
      result = List.of();
    } else {
      throw new IOException("Fetch failed: " + response.statusCode());
    }
    return result;
  }

  /**
   * Fetches only approved events.
   *
   * @return list of approved events, or an empty list when none exist or user is unauthorized
   * @throws IOException if serialization, transport, or non-handled HTTP errors occur
   */
  @Override
  public List<Event> getApprovedEvents() throws IOException, InterruptedException {
    final HttpRequest request = LocalHttpClientHelper.buildRequest("events/approved/" + LocaleUtil.getInstance().getLocale().getLanguage()).get();

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    final List<Event> result;
    if (response.statusCode() == HttpStatus.OK.code) {
      final List<EventResponseDTO> eventDtos = mapper.readValue(response.body(),
          new TypeReference<>() {
          });
      result = EventMapper.toModelList(eventDtos);
    } else if (response.statusCode() == HttpStatus.NO_CONTENT.code) {
      result = List.of();
    } else if (response.statusCode() == HttpStatus.UNAUTHORIZED.code) {
      AuthService.getInstance().logout();
      result = List.of();
    } else {
      throw new IOException("Fetch failed: " + response.statusCode());
    }

    return result;
  }

  /**
   * Creates a new event.
   *
   * @param data request payload used to create the event
   * @return created event model
   * @throws IOException if serialization, transport, or creation response handling fails
   */
  @Override
  public Event createEvent(EventDto.EventRequestDTO data) throws IOException, InterruptedException {
    final String json = mapper.writeValueAsString(data);
    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL)
        .authorization(authService.getToken())
        .post(json);
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.CREATED.code) {
      final EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
      return EventMapper.toModel(dto); // Return model
    } else {
      throw new IOException("Creation failed: " + response.body());
    }
  }

  /**
   * Updates an existing event.
   *
   * @param id   event identifier
   * @param data request payload with updated values
   * @return updated event model
   * @throws IOException if serialization, transport, or update response handling fails
   */
  @Override
  public Event updateEvent(String id, EventDto.EventRequestDTO data) throws IOException,
      InterruptedException {
    final String json = mapper.writeValueAsString(data);
    final HttpRequest request = LocalHttpClientHelper.buildRequest(BASE_URL + "/" + id)
        .authorization(authService.getToken())
        .post(json);

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final EventResponseDTO dto = mapper.readValue(response.body(),
          EventResponseDTO.class);
      return EventMapper.toModel(dto); // Return model
    } else {
      throw new IOException("Update failed: " + response.body());
    }
  }

    public Event updateLocalEvent(String id, EventDto.EventLocalRequestDTO data, Language lang)
        throws IOException, InterruptedException {
        final String json = mapper.writeValueAsString(data);

        final HttpRequest request = LocalHttpClientHelper.buildRequest(
                BASE_URL + "/" + id + "/" + lang.getLocale().getLanguage())
            .authorization(authService.getToken()).put(json);

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            final EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
            return EventMapper.toModel(dto);
        } else {
            throw new IOException("Update Failed: " + response.body());
        }
    }

  /**
   * Deletes an event by identifier.
   *
   * @param id event identifier
   * @throws IOException if transport fails or backend does not return a successful delete status
   */
  @Override
  public void deleteEvent(String id) throws IOException, InterruptedException {
    final String url = String.format("%s/%s", BASE_URL, id);
    final HttpRequest request = LocalHttpClientHelper.buildRequest(url)
        .authorization(authService.getToken())
        .delete();
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != HttpStatus.NO_CONTENT.code && response.statusCode()
        != HttpStatus.OK.code) {
      throw new IOException("Delete failed: " + response.body());
    }
  }

  /**
   * Fetches a single event by identifier.
   *
   * @param id event identifier
   * @return event model
   * @throws IOException if transport fails or event retrieval is unsuccessful
   */
  @Override
  public Event getEventById(String id) throws IOException, InterruptedException {
    final String url = String.format("%s/%s/%s", BASE_URL, id,LocaleUtil.getInstance().getLocale().getLanguage());
    final HttpRequest request = LocalHttpClientHelper.buildRequest(url).get();
    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final EventResponseDTO dto = mapper.readValue(response.body(),
          EventResponseDTO.class);
      return EventMapper.toModel(dto);
    } else {
      throw new IOException("Event not found: " + id);
    }
  }

    public Event getLocalEventById(String id, Language language) throws IOException, InterruptedException {
        final HttpRequest request = LocalHttpClientHelper.buildRequest(
            BASE_URL + "/" + id + "/" + language.getLocale().getLanguage())
            .authorization(authService.getToken()).get();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpStatus.OK.code) {
            final EventResponseDTO dto = mapper.readValue(response.body(), EventResponseDTO.class);
            return EventMapper.toModel(dto);
        } else {
            throw new IOException("Event not found: " + response.body());
        }
    }

  /**
   * Approves an event by identifier.
   *
   * @param id event identifier
   * @return approved event model
   * @throws IOException if transport fails or backend approval response is unsuccessful
   */
  @Override
  public Event approveEvent(String id) throws IOException, InterruptedException {
    final String url = String.format("%s/%s/approve", BASE_URL, id);
    final HttpRequest request = LocalHttpClientHelper.buildRequest(url)
        .authorization(authService.getToken())
        .put(null);

    final HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() == HttpStatus.OK.code) {
      final EventResponseDTO dto = mapper.readValue(response.body(),
          EventResponseDTO.class);
      return EventMapper.toModel(dto);
    } else {
      throw new IOException("Approve failed: " + response.body());
    }
  }
}
