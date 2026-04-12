package com.cems.frontend.utils;

/**
 * Minimal HTTP status enum used by frontend services for response handling.
 */
public enum HttpStatus {
  OK(200),
  CREATED(201),
  NO_CONTENT(204),
  UNAUTHORIZED(401);

  public final int code;

  HttpStatus(int code) {
    this.code = code;
  }

  /**
   * Indicates whether this status code represents a successful response.
   *
   * @return {@code true} for 2xx status codes, otherwise {@code false}
   */
  public static boolean isSuccess(int statusCode) {
    return statusCode >= 200 && statusCode < 300;
  }

  /**
   * Indicates whether the provided status code represents a client error.
   *
   * @param statusCode HTTP status code
   * @return {@code true} for 4xx status codes, otherwise {@code false}
   */
  public static boolean isClientError(int statusCode) {
    return statusCode >= 400 && statusCode < 500;
  }

  /**
   * Indicates whether the provided status code represents a server error.
   *
   * @param statusCode HTTP status code
   * @return {@code true} for 5xx status codes, otherwise {@code false}
   */
  public static boolean isServerError(int statusCode) {
    return statusCode >= 500 && statusCode < 600;
  }
}
