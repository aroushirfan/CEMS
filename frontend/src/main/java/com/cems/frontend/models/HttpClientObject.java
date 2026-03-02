package com.cems.frontend.models;

import java.net.http.HttpClient;

public class HttpClientObject {

    private static HttpClient instance = null;

    private HttpClientObject() {}

    public static HttpClient getClient() {
        if (instance == null) {
            instance = HttpClient.newHttpClient();
        }
        return instance;
    }

}
