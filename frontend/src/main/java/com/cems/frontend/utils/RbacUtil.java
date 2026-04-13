package com.cems.frontend.utils;

/**
 * Utility methods for role-based access checks using locally stored role data.
 */
public class RbacUtil {
  /**
   * Utility class constructor.
   */
  private RbacUtil() {
  }

  /**
   * Returns the currently stored user role.
   *
   * @return role name, or empty string when no role is stored
   */
  public static String getRole() {
    String role = LocalStorage.get("role");
    if (role == null || role.isEmpty()) {
      return "";
    } else {
      return role;
    }
  }

  /**
   * Checks whether the current user role is Admin.
   *
   * @return {@code true} when role is Admin, otherwise {@code false}
   */
  public static boolean isAdmin() {
    return RbacUtil.getRole().equalsIgnoreCase("Admin");
  }

  /**
   * Checks whether the current user role is Faculty.
   *
   * @return {@code true} when role is Faculty, otherwise {@code false}
   */
  public static boolean isFaculty() {
    return RbacUtil.getRole().equalsIgnoreCase("Faculty");
  }

  /**
   * Checks whether the current user role is User.
   *
   * @return {@code true} when role is User, otherwise {@code false}
   */
  public static boolean isUser() {
    return RbacUtil.getRole().equalsIgnoreCase("User");
  }

}
