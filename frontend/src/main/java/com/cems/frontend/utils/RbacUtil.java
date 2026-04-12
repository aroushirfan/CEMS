package com.cems.frontend.utils;

public class RbacUtil {
    private RbacUtil() {}

    public static String getRole() {
        String role = LocalStorage.get("role");
        if (role == null || role.isEmpty()) {
            return "";
        } else {
            return role;
        }
    }

    public static boolean isAdmin() {
        return RbacUtil.getRole().equalsIgnoreCase("Admin");
    }

    public static boolean isFaculty() {
        return RbacUtil.getRole().equalsIgnoreCase("Faculty");
    }
    public static boolean isUser() {
        return RbacUtil.getRole().equalsIgnoreCase("User");
    }

}
