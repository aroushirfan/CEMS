package com.cems.frontend.utils;

import com.cems.frontend.models.Paths;
import com.cems.frontend.view.SceneNavigator;
import javafx.application.Platform;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleUtil {
    private Locale locale;
    private static LocaleUtil localeUtil;
    private Language language;
    private Language latestLanguage;

    private LocaleUtil() {
        this.locale = Locale.US;
        this.language = Language.EN;
        this.latestLanguage = language;
    }

    public static LocaleUtil getInstance() {
        if (localeUtil == null) {
            localeUtil = new LocaleUtil();
        }
        return localeUtil;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLocale(Language language) {
        if (this.language.equals(language)) return;
        this.language = language;
        this.locale = language.getLocale();
        SceneNavigator.reloadNavigationView();
        setFont(language);
    }

    public void setFont(Language language) {
        if (latestLanguage.getFontCss() != null) {
            String fontCssPath = latestLanguage.getFontCss();
            URL resource = getClass().getResource(fontCssPath);
            if (resource != null) {
                // Proceed with your code
                SceneNavigator.getNavigationController().getContentArea().getScene().getStylesheets().removeAll(resource.toExternalForm());
            } else {
                System.out.println("Resource not found: " + fontCssPath);
            }
        }
        if (language.getFontCss() != null) {
            Platform.runLater(() -> {
                SceneNavigator.getNavigationController().getContentArea().getScene().getStylesheets().add(getClass().getResource(language.getFontCss()).toExternalForm());
            });
        }
        latestLanguage = language;
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getBundle(Paths fxmlPath) {
        return ResourceBundle.getBundle(fxmlPath.bundlePath,this.locale);
    }
}
