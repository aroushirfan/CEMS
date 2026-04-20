package com.cems.frontend.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RbacUtilTest {

  @AfterEach
  void tearDown() {
    // Clear the role after each test so it doesn't affect other tests
    LocalStorage.remove("role");
  }

  @Test
  void testGetRole_EmptyWhenNull() {
    // Ensure that if nothing is in LocalStorage, it returns an empty string
    assertEquals("", RbacUtil.getRole());
  }

  @Test
  void testIsAdmin() {
    LocalStorage.set("role", "Admin");
    assertTrue(RbacUtil.isAdmin(), "Should be Admin");
    assertFalse(RbacUtil.isFaculty());

    // Test case-insensitivity (covers .equalsIgnoreCase)
    LocalStorage.set("role", "admin");
    assertTrue(RbacUtil.isAdmin());
  }

  @Test
  void testIsFaculty() {
    LocalStorage.set("role", "Faculty");
    assertTrue(RbacUtil.isFaculty(), "Should be Faculty");
    assertFalse(RbacUtil.isAdmin());
  }

  @Test
  void testIsUser() {
    LocalStorage.set("role", "User");
    assertTrue(RbacUtil.isUser(), "Should be User");
    assertFalse(RbacUtil.isAdmin());
  }

  @Test
  void testNoRoleScenario() {
    LocalStorage.set("role", "");
    assertEquals("", RbacUtil.getRole());
    assertFalse(RbacUtil.isAdmin());
    assertFalse(RbacUtil.isFaculty());
    assertFalse(RbacUtil.isUser());
  }
}