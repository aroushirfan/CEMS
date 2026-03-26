package com.cems.frontend.models;

public enum Paths {
    ALL_EVENTS("pages/home-view.fxml","AllEvents"),
    USER_SETTINGS("pages/UserSettings.fxml","UserSettings"),
    HOME("pages/MainHome.fxml","Home"),
    EVENT_DETAIL_VIEW("pages/event-detail-view.fxml","EventDetails"),
    EDIT_VIEW("pages/edit-event-view.fxml","EditEvent"),
    ATTENDANCE_VIEW("pages/attendance-view.fxml","Attendance"),
    EVENT_MANAGEMENT("pages/admin-page.fxml","EventManagement"),
    CREATE_EVENT("pages/create-event-view.fxml","CreateEvent"),
    USER_MANAGEMENT("pages/user-management.fxml","UserManagement"),
    NAVIGATION("pages/navigation.fxml","Sidebar");

    public final String path;
    public final String bundlePath;

    private Paths(String path,String bundlePath) {
        this.path = String.format("/com/cems/frontend/view/%s", path);
        this.bundlePath = bundlePath;
    }
}
