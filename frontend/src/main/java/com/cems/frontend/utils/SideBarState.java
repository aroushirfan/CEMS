package com.cems.frontend.utils;


import com.cems.frontend.controllers.components.SidebarUserController;

public class SideBarState {
    private static SidebarUserController controller;

    public static void set(SidebarUserController c) {
        controller = c;
    }

    public static SidebarUserController get() {
        return controller;
    }
}
