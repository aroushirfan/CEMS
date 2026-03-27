package com.cems.frontend.models;

public enum Paths {
    ALL_EVENTS("pages/home-view.fxml","Bundles"),
    USER_SETTINGS("pages/UserSettings.fxml","com.cems.frontend.view.i18n.UserSettings"),
    HOME("pages/MainHome.fxml","Bundles"),
    EVENT_DETAIL_VIEW("pages/event-detail-view.fxml","com.cems.frontend.view.i18n.EventDetail"),
    EDIT_VIEW("pages/edit-event-view.fxml","Bundles"),
    ATTENDANCE_VIEW("pages/attendance-view.fxml","com.cems.frontend.view.i18n.Attendance"),
    EVENT_MANAGEMENT("pages/admin-page.fxml","Bundles"),
    CREATE_EVENT("pages/create-event-view.fxml","Bundles"),
    USER_MANAGEMENT("pages/user-management.fxml","com.cems.frontend.view.i18n.UserManagement"),
    NAVIGATION("pages/navigation.fxml","Bundles");

    public final String path;
    public final String bundlePath;

    private Paths(String path,String bundlePath) {
        this.path = String.format("/com/cems/frontend/view/%s", path);
        this.bundlePath = bundlePath;
    }
}
