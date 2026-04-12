package com.cems.frontend.utils;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    UNAUTHORIZED(401);

    public final int code;

    HttpStatus(int code) {
        this.code = code;
    }
}
