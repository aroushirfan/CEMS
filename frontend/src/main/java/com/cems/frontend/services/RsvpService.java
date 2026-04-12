package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.EventMapper;
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

public class RsvpService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RsvpService(HttpClient client, ObjectMapper mapper) {
        this.httpClient = client;
        this.objectMapper = mapper;
    }

    /**
     * Returns True or False
     * @return bool
     */
    public String checkRsvp(UUID eventId) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString()).authorization(AuthService.getInstance().getToken()).get();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        }else {
            throw new IOException("Post request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Returns a success message
     * @return String
     */
    public List<Event> getRegisteredEvents() throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/my-events").authorization(AuthService.getInstance().getToken()).get();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<EventDto.EventResponseDTO> eventResponseDTOS = objectMapper.readValue(response.body(),  new TypeReference<>() {});
            return EventMapper.toModelList(eventResponseDTOS);
        }else {
            throw new IOException("Fetch request failed with status code: " + response.statusCode());
        }
    }

    /**
     * Returns a success message
     * @return boolean
     */
    public boolean checkUserRsvp(UUID eventId) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest(String.format("rsvp/%s/registered",eventId)).authorization(AuthService.getInstance().getToken()).get();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return objectMapper.readTree(response.body()).get("registered").asBoolean();
        }else {
            throw new IOException("Fetch request failed with status code: " + response.statusCode() + "error: "+ response.body());
        }
    }

    /**
     * Returns a success message
     * @return String
     */
    public String register(UUID eventId) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString()).authorization(AuthService.getInstance().getToken()).post(null);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return objectMapper.readTree(response.body()).get("message").asText();
        }else {
            throw new IOException(objectMapper.readTree(response.body()).get("message").asText());
        }
    }

    /**
     * Returns a success message
     * @return String
     */
    public String cancelRegistration(UUID eventId) throws Exception {
        HttpRequest request = LocalHttpClientHelper.buildRequest("rsvp/" + eventId.toString()).authorization(AuthService.getInstance().getToken()).delete();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204 || response.statusCode() == 200) {
            return "Registration cancelled successfully";
        }else {
            throw new IOException("Fetch request failed with status code: " + response.statusCode());
        }
    }
}
