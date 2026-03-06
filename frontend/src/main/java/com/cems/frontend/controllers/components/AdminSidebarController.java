package com.cems.frontend.controllers.components;

import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;

public class AdminSidebarController {
    @FXML
    private void handleDashboard() {
        SceneNavigator.loadPage("admin-dashboard.fxml");
    }
    @FXML
    private void handleEvents() {
        SceneNavigator.loadPage("admin-dashboard.fxml");
    }
    @FXML
    private void handleUsers() {
        //yet to implement
    }
    @FXML
    private void handleHome() {
        SceneNavigator.loadPage("home-view.fxml");
    }
    @FXML
    private void handleLogout() {
         SceneNavigator.loadPage("Login.fxml");
    }


}
