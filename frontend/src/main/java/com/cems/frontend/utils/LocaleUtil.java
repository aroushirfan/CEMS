package com.cems.frontend.utils;

import java.util.Locale;

public class LocaleUtil {
    private Locale locale;
    public static LocaleUtil localeUtil;

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
    }

    public Locale getLocale() {
        return locale;
    }
}
