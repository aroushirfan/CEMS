package com.cems.frontend.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocalStorageTest {

  private static final String TEST_KEY = "test_key_2026";
  private static final String TEST_VALUE = "test_value_gemini";

  @AfterEach
  void tearDown() {
    // Clean up the preference node after each test so we don't pollute the OS
    LocalStorage.remove(TEST_KEY);
  }

  @Test
  void testSetAndGet() {
    // Test basic storage and retrieval
    LocalStorage.set(TEST_KEY, TEST_VALUE);

    String result = LocalStorage.get(TEST_KEY);
    assertEquals(TEST_VALUE, result, "The retrieved value should match the stored value.");
  }

  @Test
  void testGet_NonExistentKey() {

    assertNull(LocalStorage.get("non_existent_key_xyz"));
  }

  @Test
  void testPrefNodeIsAccessible() {
    assertNotNull(LocalStorage.PREF);
    assertTrue(LocalStorage.PREF.absolutePath().contains("cems"));
  }
}