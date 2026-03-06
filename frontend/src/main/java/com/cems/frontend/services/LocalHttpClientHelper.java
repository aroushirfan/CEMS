package com.cems.frontend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

public class LocalHttpClientHelper {
    private static final String BASE_URL = "http://localhost";
    private static String PORT = "8080"; // default port
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpClient getClient() {
       return httpClient;
    }

    public static ObjectMapper getMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static void setPort(String port) {
        PORT = port;
    }

    public static HttpRequest buildGetRequest (String uri) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s:%s/%s", BASE_URL,PORT, uri)))
                .header("Accept", "application/json")
                .GET()
                .build();
    }
}
