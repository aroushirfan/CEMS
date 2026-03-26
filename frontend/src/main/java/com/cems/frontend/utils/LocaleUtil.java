package com.cems.frontend.utils;

import com.cems.frontend.view.SceneNavigator;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleUtil {
    private Locale locale;
    private static LocaleUtil localeUtil;

    private LocaleUtil() {
        this.locale = Locale.US;
    }

    public static LocaleUtil getInstance() {
        if (localeUtil == null) {
            localeUtil = new LocaleUtil();
        }
        return localeUtil;
    }

    public void setLocale(Language language) {
        this.locale = language.getLocale();
        SceneNavigator.reloadNavigationView();
    }

    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("Bundles",this.locale);
    }
}
