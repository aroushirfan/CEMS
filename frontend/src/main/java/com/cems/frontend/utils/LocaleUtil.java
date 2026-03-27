package com.cems.frontend.utils;

import com.cems.frontend.models.Paths;
import com.cems.frontend.view.SceneNavigator;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleUtil {
    private Locale locale;
    private static LocaleUtil localeUtil;
    private Language language;

    private LocaleUtil() {
        this.locale = Locale.US;
        this.language = Language.EN;
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
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getBundle(Paths fxmlPath) {
        return ResourceBundle.getBundle(fxmlPath.bundlePath,this.locale);
    }
}
