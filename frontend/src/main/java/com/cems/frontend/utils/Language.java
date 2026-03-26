package com.cems.frontend.utils;

import java.util.Locale;

public enum Language {

        EN("English",Locale.US),
        TH("Thai",Locale.forLanguageTag("th-TH")),
        UR("Urdu",Locale.forLanguageTag("ur-PK"));

        private final Locale locale;
        private final String displayName;

        Language(String displayName,Locale locale) {
            this.locale = locale;
            this.displayName = displayName;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getDisplayName() {
            return displayName;
        }

    public static Language fromDisplayName(String name) {
        for (Language lang : values()) {
            if (lang.displayName.equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return EN;
    }
}
