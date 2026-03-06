package com.cems.frontend.models;

public class ResponseError {
    private String error;

    public ResponseError() {}
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
