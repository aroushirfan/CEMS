package com.cems.frontend.services;

import com.cems.frontend.utils.Language;
import com.cems.shared.model.EventDto;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.Authenticator;
import java.net.http.*;
import java.util.Optional;
import javax.net.ssl.SSLSession;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ApiEventServiceTest {
  private ApiEventService service;
  private static int stubStatusCode = 200;

  @BeforeEach
  void setUp() throws Exception {
    service = new ApiEventService();
    stubStatusCode = 200;
    Field clientField = ApiEventService.class.getDeclaredField("client");
    clientField.setAccessible(true);
    clientField.set(service, new FakeHttpClient());
  }

  @Test
  void testFullCoverage() throws Exception {
    // 1. Success Paths (200 OK)
    assertNotNull(service.getAllEvents());
    assertNotNull(service.getApprovedEvents());
    assertNotNull(service.getEventById("1"));
    assertNotNull(service.approveEvent("1"));
    service.deleteEvent("1");

    // 2. POST / PUT & Localization
    var requestDto = new EventDto.EventRequestDTO();
    var localDto = new EventDto.EventLocalRequestDTO();

    stubStatusCode = 201;
    assertNotNull(service.createEvent(requestDto));

    stubStatusCode = 200;
    assertNotNull(service.updateEvent("1", requestDto));
    assertNotNull(service.updateLocalEvent("1", localDto, Language.EN));
    assertNotNull(service.getLocalEventById("1", Language.EN));

    // 3. No Content & Unauthorized
    stubStatusCode = 204;
    assertTrue(service.getAllEvents().isEmpty());
    assertTrue(service.getApprovedEvents().isEmpty());

    stubStatusCode = 401;
    assertTrue(service.getApprovedEvents().isEmpty());

    // 4. Exception Branches
    stubStatusCode = 500;
    assertThrows(IOException.class, () -> service.getAllEvents());
    assertThrows(IOException.class, () -> service.getEventById("1"));
    assertThrows(IOException.class, () -> service.deleteEvent("1"));
  }

  private static class FakeHttpClient extends HttpClient {
    @Override
    public <T> HttpResponse<T> send(HttpRequest req, HttpResponse.BodyHandler<T> h) throws IOException, InterruptedException {
      return (HttpResponse<T>) new FakeResponse(req);
    }
    @Override public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest r, HttpResponse.BodyHandler<T> h) { return null; }
    @Override public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest r, HttpResponse.BodyHandler<T> h, HttpResponse.PushPromiseHandler<T> p) { return null; }
    @Override public Optional<CookieHandler> cookieHandler() { return Optional.empty(); }
    @Override public Optional<java.time.Duration> connectTimeout() { return Optional.empty(); }
    @Override public HttpClient.Redirect followRedirects() { return HttpClient.Redirect.NEVER; }
    @Override public Optional<ProxySelector> proxy() { return Optional.empty(); }
    @Override public javax.net.ssl.SSLContext sslContext() { return null; }
    @Override public javax.net.ssl.SSLParameters sslParameters() { return null; }
    @Override public Optional<Authenticator> authenticator() { return Optional.empty(); }
    @Override public HttpClient.Version version() { return HttpClient.Version.HTTP_2; }
    @Override public Optional<Executor> executor() { return Optional.empty(); }
  }

  private record FakeResponse(HttpRequest request) implements HttpResponse<String> {
    @Override
    public int statusCode() {
      return stubStatusCode;
    }

    @Override
    public String body() {
        String uri = request.uri() != null ? request.uri().toString() : "";
        return (uri.contains("/all/") || uri.contains("/approved/")) ? "[]" : "{}";
      }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
      return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
      return HttpHeaders.of(Collections.emptyMap(), (s1, s2) -> true);
    }

    @Override
    public Optional<SSLSession> sslSession() {
      return Optional.empty();
    }

    @Override
    public URI uri() {
      return URI.create("http://localhost");
    }

    @Override
    public HttpClient.Version version() {
      return HttpClient.Version.HTTP_2;
    }
    }
}