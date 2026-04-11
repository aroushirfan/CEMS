package com.cems.frontend.models;

/**
 * A simple model class to represent an error response from the backend.
 */
public class ResponseError {
  private String error;

  /** Default constructor for JSON deserialization. */
  public ResponseError() {
  }

  /** Constructor to create a ResponseError with a specific error message. */
  public ResponseError(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
