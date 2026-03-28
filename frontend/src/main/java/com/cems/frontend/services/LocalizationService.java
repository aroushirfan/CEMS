package com.cems.frontend.services;

import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.Language;
import com.cems.frontend.view.SceneNavigator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {
    private Locale locale;
    private Language language;
    private static LocalizationService localizationService;

    private LocalizationService() {
        this.locale = Locale.US;
        this.language = Language.EN;
    }

    public static LocalizationService getInstance() {
        if (localizationService == null) {
            localizationService = new LocalizationService();
        }
        return localizationService;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLocale(Language language) {
        if (this.language.equals(language)) return;
        this.language = language;
        this.locale = language.getLocale();
        SceneNavigator.reloadNavigationView();
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getBundle(Paths fxmlPath) {
        return ResourceBundle.getBundle(fxmlPath.bundlePath,this.locale);
    }

    public void getLocalizedStrings(Paths fxmlPath) {
        Map<String, String> strings = new HashMap<>();
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(
                    fxmlPath.bundlePath,
                    this.locale
            );
            // Extract all keys
            for (String key : bundle.keySet()) {
                strings.put(key, bundle.getString(key));
            }
        } catch (Exception e) {
            System.err.println("Failed to load resource bundle for locale: " + locale);
        }
    }
}