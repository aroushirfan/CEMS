package com.cems.frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * Helper for creating HTTP requests with shared client and JSON configuration.
 *
 * <p>This class provides lazily initialized singleton instances for
 * {@link HttpClient} and {@link ObjectMapper}, plus a fluent API for creating
 * request objects with default headers.</p>
 */
public class LocalHttpClientHelper {
  private static String baseURL = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8080");
  private static HttpClient httpClient = null;
  private static ObjectMapper objectMapper;
  private HttpRequest.Builder requestBuilder;

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer %s";
  private static final String APPLICATION_JSON = "application/json";
  private static final String ACCEPT_HEADER = "Accept";
  private static final String CONTENT_TYPE_HEADER = "Content-Type";

  /**
   * Creates a request helper around the given request builder.
   *
   * @param request underlying request builder
   */
  private LocalHttpClientHelper(HttpRequest.Builder request) {
    this.requestBuilder = request;
  }

  /**
   * Returns a shared HTTP client instance.
   *
   * @return shared {@link HttpClient}
   */
  public static HttpClient getClient() {
    if (httpClient == null) {
      httpClient = HttpClient.newHttpClient();
    }
    return httpClient;
  }

  /**
   * Returns a shared object mapper configured for project JSON conventions.
   *
   * @return shared Jackson {@link ObjectMapper}
   */
  public static ObjectMapper getMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    return objectMapper;
  }

  /**
   * Overrides the backend base URL to localhost with the provided port.
   *
   * @param port backend port value
   */
  public static void setPort(String port) {
    baseURL = String.format("http://localhost:%s", port);
  }

  /**
   * Creates a request helper for a backend-relative URI with default JSON accept header.
   *
   * @param uri backend-relative path segment
   * @return request helper for fluent request construction
   */
  public static LocalHttpClientHelper buildRequest(String uri) {
    return new LocalHttpClientHelper(HttpRequest.newBuilder()
            .uri(URI.create(String.format("%s/%s", baseURL, uri)))
            .header(ACCEPT_HEADER, APPLICATION_JSON)
    );
  }

  /**
   * Adds a bearer authorization header to the request.
   *
   * @param token access token value
   * @return current helper instance for fluent chaining
   */
  public LocalHttpClientHelper authorization(String token) {
    this.requestBuilder = this.requestBuilder.header(AUTHORIZATION_HEADER,
            String.format(BEARER_PREFIX, token));
    return this;
  }

  /**
   * Builds a GET request.
   *
   * @return built GET request
   */
  public HttpRequest get() {
    this.requestBuilder = this.requestBuilder.GET();
    return this.build();
  }

  /**
   * Builds a POST request with optional JSON body.
   *
   * @param body request body as JSON string, or {@code null} for no body
   * @return built POST request
   */
  public HttpRequest post(String body) {
    this.requestBuilder = this.requestBuilder.header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        .POST(body != null ? HttpRequest.BodyPublishers.ofString(body)
            : HttpRequest.BodyPublishers.noBody());
    return this.build();
  }

  /**
   * Builds a PUT request with optional JSON body.
   *
   * @param body request body as JSON string, or {@code null} for no body
   * @return built PUT request
   */
  public HttpRequest put(String body) {
    this.requestBuilder = this.requestBuilder.header(CONTENT_TYPE_HEADER, APPLICATION_JSON)
        .PUT(body != null ? HttpRequest.BodyPublishers.ofString(body)
            : HttpRequest.BodyPublishers.noBody());
    return this.build();
  }

  /**
   * Builds a DELETE request.
   *
   * @return built DELETE request
   */
  public HttpRequest delete() {
    this.requestBuilder = this.requestBuilder.DELETE();
    return this.build();
  }

  /**
   * Finalizes and returns the configured request.
   *
   * @return built HTTP request
   */
  private HttpRequest build() {
    return this.requestBuilder.build();
  }
}
