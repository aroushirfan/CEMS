package com.cems.frontend.models;

public enum Paths {
    ALL_EVENTS("pages/home-view.fxml"),
    USER_SETTINGS("pages/UserSettings.fxml"),
    HOME("pages/MainHome.fxml"),
    EVENT_DETAIL_VIEW("pages/event-detail-view.fxml"),
    EDIT_VIEW("pages/edit-event-view.fxml"),
    ATTENDANCE_VIEW("pages/attendance-view.fxml"),
    SIDEBAR("components/sidebar.fxml");

    public final String path;

    private Paths(String path) {
        this.path = String.format("/com/cems/frontend/view/%s", path);
    }
}
