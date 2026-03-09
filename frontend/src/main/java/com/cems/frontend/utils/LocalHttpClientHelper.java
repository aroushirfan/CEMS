package com.cems.frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

public class LocalHttpClientHelper {
    private static final String BASE_URL = "http://localhost";
    private static String PORT = System.getenv().getOrDefault("PORT","8080"); // default port
    private static HttpClient httpClient;
    private static ObjectMapper objectMapper;

    public static HttpClient getClient() {
        if (httpClient == null) {
            httpClient = HttpClient.newHttpClient();
        }
       return httpClient;
    }

    public static ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
        return objectMapper;
    }

    public static void setPort(String port) {
        PORT = port;
    }

    public static HttpRequest buildGetRequest (String uri,String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s:%s/%s", BASE_URL,PORT, uri)))
                .header("Accept", "application/json")
                .header("Authorization", String.format("Bearer %s", token))
                .GET()
                .build();
    }

    public static HttpRequest buildPostRequest (String uri,String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s:%s/%s", BASE_URL,PORT, uri)))
                .header("Accept", "application/json")
                .header("Authorization", String.format("Bearer %s", token))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
    }

    public static HttpRequest buildDeleteRequest (String uri,String token) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s:%s/%s", BASE_URL,PORT, uri)))
                .header("Accept", "application/json")
                .header("Authorization", String.format("Bearer %s", token))
                .DELETE()
                .build();
    }
}
