package com.cems.frontend.utils;


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
  @Test
  void testGetFontCss() {
    // This covers the 3-parameter constructor
    assertEquals("/com/cems/frontend/view/css/thai.css", Language.TH.getFontCss());
    // This covers the 2-parameter constructor (where fontCss is null)
    assertNull(Language.EN.getFontCss());
  }

  @Test
  void testFromDisplayName() {
    // Test successful matches (Case-insensitive)
    assertEquals(Language.EN, Language.fromDisplayName("English"));
    assertEquals(Language.EN, Language.fromDisplayName("english"));
    assertEquals(Language.TH, Language.fromDisplayName("ภาษาไทย"));

    // Test the FAIL path (IMPORTANT for SonarQube)
    // This ensures the code reaches the final "return EN;" line
    assertEquals(Language.EN, Language.fromDisplayName("Not A Real Language"));
    assertEquals(Language.EN, Language.fromDisplayName(null));
  }

  @Test
  void testToString() {
    // You overridden toString(), so you must test it
    assertEquals("English", Language.EN.toString());
  }

  @Test
  void testGetAllLanguages() {
    Language[] all = Language.getAllLanguages();
    assertEquals(3, all.length);
    assertEquals(Language.EN, all[0]);
  }

  @Test
  void testEnumBoilerplate() {
    assertNotNull(Language.values());
    assertEquals(Language.EN, Language.valueOf("EN"));
  }
}