package com.cems.cemsbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CaseConversionServiceTest {

    @InjectMocks
    private CaseConversionService caseConversionService;

    @BeforeEach
    void setUp() {
        // Service is already instantiated by @InjectMocks
    }

    @Test
    void camelToSnake_WithSimpleCamelCase_ConvertsCorrectly() {
        String input = "firstName";
        String expected = "first_name";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithMultipleCamelWords_ConvertsCorrectly() {
        String input = "getUserByEmailAddress";
        String expected = "get_user_by_email_address";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithSingleWord_ReturnsLowercase() {
        String input = "name";
        String expected = "name";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithMixedCase_ConvertsToSnake() {
        String input = "userIdNumber";
        String expected = "user_id_number";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithConsecutiveCapitals_ConvertsCorrectly() {
        String input = "HTTPStatusCode";
        String expected = "httpstatus_code";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithEmptyString_ReturnsEmpty() {
        String input = "";
        String expected = "";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithNull_ReturnsNull() {
        String input = null;

        String result = caseConversionService.camelToSnake(input);

        assertNull(result);
    }

    @Test
    void camelToSnake_WithSingleCharacter_ReturnsLowercase() {
        String input = "A";
        String expected = "a";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithNumbersInString_PreservesNumbers() {
        String input = "user2NameId123";
        String expected = "user2name_id123";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithLeadingCapital_ConvertsCorrectly() {
        String input = "FirstName";
        String expected = "first_name";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithAlreadySnakeCase_HandlesCorrectly() {
        String input = "first_name";
        String expected = "first_name";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithUnderscoresAndCamelCase_ConvertsCorrectly() {
        String input = "user_firstName";
        String expected = "user_first_name";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }

    @Test
    void camelToSnake_WithSpecialCharacters_ConvertsCamelPortion() {
        String input = "firstName@domain";
        String expected = "first_name@domain";

        String result = caseConversionService.camelToSnake(input);

        assertEquals(expected, result);
    }
}
