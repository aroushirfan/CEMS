package com.cems.frontend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocalHttpClientHelperTest {

  @Test
  void testGetClientAndMapper() {

    HttpClient client1 = LocalHttpClientHelper.getClient();
    HttpClient client2 = LocalHttpClientHelper.getClient();
    assertNotNull(client1);
    assertSame(client1, client2, "HttpClient should be a singleton");

    ObjectMapper mapper1 = LocalHttpClientHelper.getMapper();
    ObjectMapper mapper2 = LocalHttpClientHelper.getMapper();
    assertNotNull(mapper1);
    assertSame(mapper1, mapper2, "ObjectMapper should be a singleton");
  }

  @Test
  void testBuildRequestAndPort() {

    LocalHttpClientHelper.setPort("9090");
    HttpRequest request = LocalHttpClientHelper.buildRequest("test-endpoint").get();

    assertEquals("http://localhost:9090/test-endpoint", request.uri().toString());
    assertEquals("application/json", request.headers().firstValue("Accept").orElse(""));
  }

  @Test
  void testAuthorizationHeader() {
    HttpRequest request = LocalHttpClientHelper.buildRequest("secure")
            .authorization("my-token")
            .get();

    assertEquals("Bearer my-token", request.headers().firstValue("Authorization").orElse(""));
  }

  @Test
  void testGetAndDelete() {
    HttpRequest getReq = LocalHttpClientHelper.buildRequest("api").get();
    assertEquals("GET", getReq.method());

    HttpRequest deleteReq = LocalHttpClientHelper.buildRequest("api").delete();
    assertEquals("DELETE", deleteReq.method());
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  void testPostAndPutWithBody() {
    String jsonBody = "{\"name\":\"test\"}";


    HttpRequest postReq = LocalHttpClientHelper.buildRequest("api").post(jsonBody);
    assertEquals("POST", postReq.method());
    assertEquals("application/json", postReq.headers().firstValue("Content-Type").get());


    HttpRequest putReq = LocalHttpClientHelper.buildRequest("api").put(null);
    assertEquals("PUT", putReq.method());
  }
}