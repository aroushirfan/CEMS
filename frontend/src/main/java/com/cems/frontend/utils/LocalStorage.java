package com.cems.frontend.utils;

import com.cems.frontend.Launcher;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class LocalStorage {

    public static final Preferences PREF = Preferences.userNodeForPackage(Launcher.class);

    public static void set(String key, String value) {
        PREF.put(key, value);
        try {
            PREF.flush();  // Explicitly flush changes to disk
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
    public static String get(String key) {return PREF.get(key, null);}
    public static void remove(String key) {PREF.remove(key);}
}
