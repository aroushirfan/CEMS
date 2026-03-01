package com.cems.frontend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpRequest;

public class LocalHttpClient {
    private static final String BASE_URL = "http://localhost:8080/";
    public static java.net.http.HttpClient  getClient() {
        return java.net.http.HttpClient.newHttpClient();
    }

    public static ObjectMapper  getMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static HttpRequest buildGetRequest (String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + uri))
                .header("Accept", "application/json")
                .GET()
                .build();
    }

}
