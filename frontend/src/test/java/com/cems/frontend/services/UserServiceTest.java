package com.cems.frontend.services;

import com.cems.shared.model.UserDTO;
import org.junit.jupiter.api.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.*;
import java.util.Optional;
import javax.net.ssl.SSLSession;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
  private UserService userService;
  private static int stubStatusCode = 200;

  @BeforeEach
  void setUp() throws Exception {
    userService = new UserService();
    stubStatusCode = 200;
    Field clientField = UserService.class.getDeclaredField("client");
    clientField.setAccessible(true);
    clientField.set(userService, new FakeHttpClient());
  }

  @Test
  void testFullCoverage() throws Exception {
    // 1. Success Paths
    assertNotNull(userService.getCurrentUser());
    assertFalse(userService.getAllUsers().isEmpty());
    assertNotNull(userService.getUserById("123"));

    UserDTO dto = new UserDTO();
    assertNotNull(userService.updateCurrentUser(dto));
    assertDoesNotThrow(() -> userService.updateAccessLevel("123", 1));
    assertDoesNotThrow(() -> userService.deleteCurrentUser());

    // 2. No Content Branch
    stubStatusCode = 204;
    assertTrue(userService.getAllUsers().isEmpty());

    // 3. Exception Branches
    stubStatusCode = 500;
    assertThrows(IOException.class, () -> userService.getCurrentUser());
    assertThrows(IOException.class, () -> userService.updateCurrentUser(dto));
    assertThrows(IOException.class, () -> userService.getAllUsers());
    assertThrows(IOException.class, () -> userService.getUserById("123"));
    assertThrows(IOException.class, () -> userService.updateAccessLevel("123", 1));
    assertThrows(IOException.class, () -> userService.deleteCurrentUser());
  }

  private static class FakeHttpClient extends HttpClient {
    @Override
    public <T> HttpResponse<T> send(HttpRequest req, HttpResponse.BodyHandler<T> h) throws IOException, InterruptedException {
      return (HttpResponse<T>) new FakeResponse(req);
    }
    @Override public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest r, HttpResponse.BodyHandler<T> h) { return null; }
    @Override public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest r, HttpResponse.BodyHandler<T> h, HttpResponse.PushPromiseHandler<T> p) { return null; }
    @Override public Optional<java.net.CookieHandler> cookieHandler() { return Optional.empty(); }
    @Override public Optional<java.time.Duration> connectTimeout() { return Optional.empty(); }
    @Override public HttpClient.Redirect followRedirects() { return HttpClient.Redirect.NEVER; }
    @Override public Optional<java.net.ProxySelector> proxy() { return Optional.empty(); }
    @Override public javax.net.ssl.SSLContext sslContext() { return null; }
    @Override public javax.net.ssl.SSLParameters sslParameters() { return null; }
    @Override public Optional<java.net.Authenticator> authenticator() { return Optional.empty(); }
    @Override public HttpClient.Version version() { return HttpClient.Version.HTTP_2; }
    @Override public Optional<Executor> executor() { return Optional.empty(); }
  }

  private static class FakeResponse implements HttpResponse<String> {
    private final HttpRequest request;
    public FakeResponse(HttpRequest request) { this.request = request; }
    @Override public int statusCode() { return stubStatusCode; }
    @Override public String body() {
      String uri = request.uri() != null ? request.uri().toString() : "";
      if (uri.endsWith("users")) return "[{\"email\":\"test@test.com\"}]";
      return "{\"email\":\"test@test.com\",\"access_level\":1}";
    }
    @Override public HttpRequest request() { return request; }
    @Override public Optional<HttpResponse<String>> previousResponse() { return Optional.empty(); }
    @Override public HttpHeaders headers() { return HttpHeaders.of(Collections.emptyMap(), (s1, s2) -> true); }
    @Override public Optional<SSLSession> sslSession() { return Optional.empty(); }
    @Override public URI uri() { return URI.create("http://localhost"); }
    @Override public HttpClient.Version version() { return HttpClient.Version.HTTP_2; }
  }
}