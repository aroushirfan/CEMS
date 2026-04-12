package com.cems.frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;

public class LocalHttpClientHelper {
    private static String baseURL = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8080");
    private static HttpClient httpClient = null;
    private static ObjectMapper objectMapper;
    private HttpRequest.Builder requestBuilder;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer %s";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT_HEADER =  "Accept";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private LocalHttpClientHelper(HttpRequest.Builder request) {
        this.requestBuilder = request;
    }

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
        baseURL = String.format("http://localhost:%s", port);
    }

    public static LocalHttpClientHelper buildRequest (String uri) {
        return new LocalHttpClientHelper(HttpRequest.newBuilder().
                uri(URI.create(String.format("%s/%s", baseURL, uri))).
                header(ACCEPT_HEADER, APPLICATION_JSON)
        );
    }

    public LocalHttpClientHelper authorization(String token) {
        this.requestBuilder = this.requestBuilder.header(AUTHORIZATION_HEADER, String.format(BEARER_PREFIX, token));
        return this;
    }

    public HttpRequest get () {
        this.requestBuilder = this.requestBuilder.GET();
        return this.build();
    }

    public HttpRequest post (String body) {
        this.requestBuilder = this.requestBuilder.header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .POST(body != null ? HttpRequest.BodyPublishers.ofString(body)
                        :HttpRequest.BodyPublishers.noBody());
        return this.build();
    }

    public HttpRequest put (String body) {
        this.requestBuilder = this.requestBuilder.
                header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
                .PUT(body != null ?HttpRequest.BodyPublishers.ofString(body)
                        :HttpRequest.BodyPublishers.noBody());
        return this.build();
    }

    public HttpRequest delete () {
        this.requestBuilder = this.requestBuilder.DELETE();
        return this.build();
    }

    private HttpRequest build () {
        return this.requestBuilder.build();
    }
}
