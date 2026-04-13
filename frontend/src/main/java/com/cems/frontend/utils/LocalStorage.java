package com.cems.frontend.utils;

import com.cems.frontend.Launcher;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Utility for storing and retrieving simple key-value data in user preferences.
 */
public final class LocalStorage {

  /**
   * Shared preferences node scoped to the application package.
   */
  public static final Preferences PREF = Preferences.userNodeForPackage(Launcher.class);
  private static final Logger logger = Logger.getLogger(LocalStorage.class.getName());

  /**
   * Utility class constructor.
   */
  private LocalStorage() {
  }

  /**
   * Stores a key-value pair and flushes preferences to disk.
   *
   * @param key   preference key
   * @param value preference value
   */
  public static void set(String key, String value) {
    PREF.put(key, value);
    try {
      PREF.flush();  // Explicitly flush changes to disk
    } catch (BackingStoreException e) {
      logger.warning(e.getMessage());
    }
  }

  /**
   * Retrieves a value for the given key.
   *
   * @param key preference key
   * @return stored value, or {@code null} when the key does not exist
   */
  public static String get(String key) {
    return PREF.get(key, null);
  }

  /**
   * Removes a stored key and its value.
   *
   * @param key preference key to remove
   */
  public static void remove(String key) {
    PREF.remove(key);
  }
}
