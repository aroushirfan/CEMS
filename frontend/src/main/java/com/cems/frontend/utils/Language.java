package com.cems.frontend.utils;

import java.util.Locale;

public enum Language {

        EN("English",Locale.US),
        TH("Thai",Locale.forLanguageTag("th-TH"), "/com/cems/frontend/view/css/thai.css"),
        UR("Urdu",Locale.forLanguageTag("ur-PK"));

        private final Locale locale;
        private final String displayName;

        private final String fontCss;

        Language(String displayName,Locale locale, String fontCss) {
            this.locale = locale;
            this.displayName = displayName;
            this.fontCss = fontCss;
        }

        Language(String displayName, Locale locale) {
            this.locale = locale;
            this.displayName = displayName;
            this.fontCss = null;
        }

        public Locale getLocale() {
            return locale;
        }

        public String getDisplayName() {
            return displayName;
        }

    public String getFontCss() {
        return fontCss;
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
