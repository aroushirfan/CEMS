package com.cems.frontend.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void testGetLocale() {
        assertEquals(Locale.US,Language.EN.getLocale());
        assertEquals(Locale.forLanguageTag("th-TH"),Language.TH.getLocale());
        assertEquals(Locale.forLanguageTag("ur-PK"),Language.UR.getLocale());
    }

    @Test
    void fromDisplayName() {
        assertEquals(Language.EN,Language.fromDisplayName("English"));
        assertEquals(Language.TH,Language.fromDisplayName("ภาษาไทย"));
        assertEquals(Language.UR,Language.fromDisplayName("اردو"));
    }
}