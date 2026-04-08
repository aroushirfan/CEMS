package com.cems.frontend.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LocaleUtilTest {
    LocaleUtil localeUtil = LocaleUtil.getInstance();

    @Test
    void testDateTimeFormatter() {
        DateTimeFormatter formatter =
                localeUtil.dateTime(FormatStyle.SHORT, FormatStyle.SHORT);

        LocalDateTime dateTime = LocalDateTime.of(2024, 3, 15, 14, 30);

        String formatted = formatter.format(dateTime);

        assertNotNull(formatter);
        assertTrue(formatted.contains("3/15/24") || formatted.contains("03/15/24"));
    }

    @Test
    void testDateFormatter() {
        DateTimeFormatter formatter =
                localeUtil.date(FormatStyle.MEDIUM);

        LocalDate date = LocalDate.of(2024, 3, 15);

        String formatted = formatter.format(date);

        assertNotNull(formatter);
        assertTrue(formatted.contains("Mar") || formatted.contains("15"));
    }

    @Test
    void testTimeFormatter() {
        DateTimeFormatter formatter =
                localeUtil.time(FormatStyle.SHORT);

        LocalTime time = LocalTime.of(14, 30);

        String formatted = formatter.format(time);

        assertNotNull(formatter);
        assertTrue(formatted.contains("2:30") || formatted.contains("14:30"));
    }

}