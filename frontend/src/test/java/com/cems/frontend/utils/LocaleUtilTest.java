package com.cems.frontend.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


import static org.junit.jupiter.api.Assertions.*;

class LocaleUtilTest {
  private LocaleUtil localeUtil;

  @BeforeEach
  void setUp() {

    localeUtil = LocaleUtil.getInstance();
  }

  @Test
  void testSingletonInstance() {
    LocaleUtil instance1 = LocaleUtil.getInstance();
    LocaleUtil instance2 = LocaleUtil.getInstance();

    assertNotNull(instance1);
    assertSame(instance1, instance2, "Singleton should return the same instance");
  }

  @Test
  void testInitialLocaleAndLanguage() {

    assertNotNull(localeUtil.getLocale());
    assertNotNull(localeUtil.getLanguage());
  }

  @Test
  void testSetLocaleSameLanguageBranch() {
    Language current = localeUtil.getLanguage();
    localeUtil.setLocale(current);
    assertEquals(current, localeUtil.getLanguage());
  }

  @Test
  void testFormattersWithCurrentLocale() {
    LocalDateTime now = LocalDateTime.now();

    DateTimeFormatter dt = localeUtil.dateTime(FormatStyle.SHORT, FormatStyle.SHORT);
    assertNotNull(dt);
    assertFalse(dt.format(now).isEmpty());


    DateTimeFormatter d = localeUtil.date(FormatStyle.SHORT);
    assertNotNull(d);
    assertFalse(d.format(now.toLocalDate()).isEmpty());


    DateTimeFormatter t = localeUtil.time(FormatStyle.SHORT);
    assertNotNull(t);
    assertFalse(t.format(now.toLocalTime()).isEmpty());
  }
}