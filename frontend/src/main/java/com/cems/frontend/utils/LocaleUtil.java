package com.cems.frontend.utils;

import com.cems.frontend.models.Paths;
import com.cems.frontend.view.SceneNavigator;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;

/**
 * Singleton utility for managing application locale, language resources, and localized formatters.
 */
public final class LocaleUtil {
  private Locale locale;
  private static LocaleUtil localeUtilInstance = null;
  private Language language;
  private Language latestLanguage;

  private LocaleUtil() {
    this.locale = Locale.US;
    this.language = Language.EN;
    this.latestLanguage = language;
  }

  /**
   * Returns the singleton instance of the locale utility.
   *
   * @return shared {@link LocaleUtil} instance
   */
  public static LocaleUtil getInstance() {
    if (localeUtilInstance == null) {
      localeUtilInstance = new LocaleUtil();
    }
    return localeUtilInstance;
  }

  /**
   * Returns the current application language.
   *
   * @return current language configuration
   */
  public Language getLanguage() {
    return language;
  }

  /**
   * Updates the active language and locale, then refreshes navigation UI and font stylesheet.
   *
   * @param language target language configuration
   */
  public void setLocale(Language language) {
    if (this.language == language) {
      return;
    }
    this.language = language;
    this.locale = language.getLocale();
    SceneNavigator.reloadNavigationView();
    setFont(language);
  }

  /**
   * Applies font stylesheet changes for language switches.
   *
   * <p>Removes the previous language-specific stylesheet if present and applies
   * the new language stylesheet when configured.</p>
   *
   * @param language target language whose stylesheet should be applied
   */
  public void setFont(Language language) {
    if (latestLanguage.getFontCss() != null) {
      final String fontCssPath = latestLanguage.getFontCss();
      final URL resource = getClass().getResource(fontCssPath);
      if (resource != null) {
        // Proceed with your code
        SceneNavigator.getNavigationController().getContentArea()
            .getScene().getStylesheets()
            .removeAll(resource.toExternalForm());
      } else {
        System.err.println("Resource not found: " + fontCssPath);
      }
    }
    if (language.getFontCss() != null) {
      Platform.runLater(() ->
        SceneNavigator.getNavigationController().getContentArea()
            .getScene().getStylesheets().add(getClass().getResource(language.getFontCss())
                .toExternalForm())
      );
    }
    latestLanguage = language;
  }

  /**
   * Returns the current locale used for localization.
   *
   * @return current locale
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Loads a resource bundle for the specified view path using the current locale.
   *
   * @param fxmlPath view path metadata containing bundle base name
   * @return localized resource bundle
   */
  public ResourceBundle getBundle(Paths fxmlPath) {
    return ResourceBundle.getBundle(fxmlPath.bundlePath, this.locale);
  }

  /**
   * Creates a localized date-time formatter for the current locale.
   *
   * @param dateStyle date formatting style
   * @param timeStyle time formatting style
   * @return localized date-time formatter
   */
  public DateTimeFormatter dateTime(
      FormatStyle dateStyle,
      FormatStyle timeStyle
  ) {
    return DateTimeFormatter
        .ofLocalizedDateTime(dateStyle, timeStyle)
        .withLocale(locale);
  }

  /**
   * Creates a localized date formatter for the current locale.
   *
   * @param style date formatting style
   * @return localized date formatter
   */
  public DateTimeFormatter date(FormatStyle style) {
    return DateTimeFormatter
        .ofLocalizedDate(style)
        .withLocale(locale);
  }

  /**
   * Creates a localized time formatter for the current locale.
   *
   * @param style time formatting style
   * @return localized time formatter
   */
  public DateTimeFormatter time(FormatStyle style) {
    return DateTimeFormatter
        .ofLocalizedTime(style)
        .withLocale(locale);
  }
}
