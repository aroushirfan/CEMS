package com.cems.frontend.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HttpStatusTest {

  @Test
  void testEnumConstantsAndInternalMethods() {
    // Test codes
    assertEquals(200, HttpStatus.OK.code);
    assertEquals(201, HttpStatus.CREATED.code);
    assertEquals(204, HttpStatus.NO_CONTENT.code);
    assertEquals(401, HttpStatus.UNAUTHORIZED.code);

    // Coverage for built-in enum methods
    assertNotNull(HttpStatus.values());
    assertEquals(HttpStatus.OK, HttpStatus.valueOf("OK"));
  }

  @Test
  void testIsSuccess() {
    // Successful cases (2xx)
    assertTrue(HttpStatus.isSuccess(200), "200 should be success");
    assertTrue(HttpStatus.isSuccess(201), "201 should be success");
    assertTrue(HttpStatus.isSuccess(299), "299 should be success");

    // Failure cases
    assertFalse(HttpStatus.isSuccess(199), "199 should not be success");
    assertFalse(HttpStatus.isSuccess(300), "300 should not be success");
    assertFalse(HttpStatus.isSuccess(500), "500 should not be success");
  }

  @Test
  void testIsClientError() {
    // Client error cases (4xx)
    assertTrue(HttpStatus.isClientError(400), "400 should be client error");
    assertTrue(HttpStatus.isClientError(401), "401 should be client error");
    assertTrue(HttpStatus.isClientError(499), "499 should be client error");

    // Non-client error cases
    assertFalse(HttpStatus.isClientError(200), "200 is not a client error");
    assertFalse(HttpStatus.isClientError(399), "399 is not a client error");
    assertFalse(HttpStatus.isClientError(500), "500 is not a client error");
  }

  @Test
  void testIsServerError() {
    // Server error cases (5xx)
    assertTrue(HttpStatus.isServerError(500), "500 should be server error");
    assertTrue(HttpStatus.isServerError(503), "503 should be server error");
    assertTrue(HttpStatus.isServerError(599), "599 should be server error");

    // Non-server error cases
    assertFalse(HttpStatus.isServerError(200), "200 is not a server error");
    assertFalse(HttpStatus.isServerError(499), "499 is not a server error");
    assertFalse(HttpStatus.isServerError(600), "600 is not a server error");
  }
}