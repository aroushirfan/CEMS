package com.cems.frontend.models;

import java.net.http.HttpClient;

/**
 * Singleton class to provide a single instance of HttpClient throughout the application.
 */
public class HttpClientObject {

  private static HttpClient instance = null;

  /**
   * Private constructor to prevent instantiation of the class from outside.
   */
  private HttpClientObject() {
  }

  /**
   * Method to get the single instance of HttpClient.
   * If the instance does not exist, it creates one.
   *
   * @return The single instance of HttpClient.
   */
  public static HttpClient getClient() {
    if (instance == null) {
      instance = HttpClient.newHttpClient();
    }
    return instance;
  }
}