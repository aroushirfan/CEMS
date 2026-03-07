package com.cems.frontend.utils;


import com.cems.frontend.controllers.components.SidebarController;

public class SideBarState {
    private static SidebarController controller;

    public static void set(SidebarController c) {
        controller = c;
    }

    public static SidebarController get() {
        return controller;
    }
}
