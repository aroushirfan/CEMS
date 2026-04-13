package com.cems.frontend.utils;

import java.util.Locale;

/**
 * Supported UI languages with locale and optional language-specific font stylesheet.
 */
public enum Language {

  /**
   * English language configuration.
   */
  EN("English", Locale.US),
  /**
   * Thai language configuration with dedicated font stylesheet.
   */
  TH("ภาษาไทย", Locale.forLanguageTag("th-TH"), "/com/cems/frontend/view/css/thai.css"),
  /**
   * Urdu language configuration.
   */
  UR("اردو", Locale.forLanguageTag("ur-PK"));

  private final Locale locale;
  private final String displayName;

  private final String fontCss;

  /**
   * Creates a language definition with optional font stylesheet.
   *
   * @param displayName language name shown in UI
   * @param locale      Java locale for the language
   * @param fontCss     path to language-specific CSS font file
   */
  Language(String displayName, Locale locale, String fontCss) {
    this.locale = locale;
    this.displayName = displayName;
    this.fontCss = fontCss;
  }

  /**
   * Creates a language definition without a font stylesheet.
   *
   * @param displayName language name shown in UI
   * @param locale      Java locale for the language
   */
  Language(String displayName, Locale locale) {
    this.locale = locale;
    this.displayName = displayName;
    this.fontCss = null;
  }

  /**
   * Returns the locale associated with the language.
   *
   * @return language locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Returns the display name of the language.
   *
   * @return language display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Returns the optional font stylesheet path.
   *
   * @return stylesheet path, or {@code null} when no custom stylesheet is configured
   */
  public String getFontCss() {
    return fontCss;
  }

  /**
   * Resolves a language by display name.
   *
   * @param name display name to match (case-insensitive)
   * @return matching language, or {@link #EN} when no match is found
   */
  public static Language fromDisplayName(String name) {
    for (final Language lang : values()) {
      if (lang.displayName.equalsIgnoreCase(name)) {
        return lang;
      }
    }
    return EN;
  }

    @Override
    public String toString() {
        return displayName;
    }

    public static Language[] getAllLanguages() {
            return new Language[]{EN, TH, UR};
    }
}
