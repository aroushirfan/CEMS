package com.cems.frontend.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResponseErrorTest {

    @Test
    void testErrorMessageSetter() {
        ResponseError responseError = new ResponseError();
        assertNull(responseError.getError());
        responseError.setError("Invalid credentials");
        assertEquals("Invalid credentials", responseError.getError());
    }

    @Test
    void testConstructorSetsErrorMessage() {
        final String error = "Event not found";
        ResponseError responseError = new ResponseError(error);
        assertEquals(error, responseError.getError());
    }
}
