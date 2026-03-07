//package com.cems.frontend.controllers.components;
//
//import com.cems.frontend.utils.LocalStorage;
//import javafx.fxml.FXML;
//import javafx.scene.Scene;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.layout.VBox;
//import com.cems.frontend.view.SceneNavigator;
//
//public class SidebarController {
//
//    @FXML private VBox sidebarRoot;
//    @FXML private ToggleButton darkModeToggle;
//
//    @FXML
//    private void handleDarkMode() {
//        Scene scene = sidebarRoot.getScene();
//        if (scene == null) return;
//
//        if (darkModeToggle.isSelected()) {
//            darkModeToggle.setText("ON");
//            applyDarkMode(scene);
//        } else {
//            darkModeToggle.setText("OFF");
//            removeDarkMode(scene);
//        }
//    }
//
//    private void applyDarkMode(Scene scene) {
//        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
//        if (!scene.getStylesheets().contains(darkTheme)) {
//            scene.getStylesheets().add(darkTheme);
//        }
//    }
//
//    private void removeDarkMode(Scene scene) {
//        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
//        scene.getStylesheets().remove(darkTheme);
//    }
//
//    @FXML
//    private void goToHome() {
//        SceneNavigator.loadPage("MainHome.fxml");
////        SceneNavigator.loadPage("home-view.fxml");
//    }
//
//    @FXML
//    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
//    }
//
//    @FXML
//    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
//    }
//
////    @FXML
////    private void goToSettings() {
////        SceneNavigator.loadPage("/com/cems/frontend/view/pages/UserSettings.fxml");
////    }
//    @FXML
//    private void goToSettings() {
//         System.out.println("Settings clicked!");
//         SceneNavigator.loadPage("/com/cems/frontend/view/pages/UserSettings.fxml");
//}
//
//    @FXML
//    private void handleLogout() {
//        LocalStorage.set("token", "");
//        SceneNavigator.loadPage("login-view.fxml");
//    }
//}
//
//package com.cems.frontend.controllers.components;
//
//import com.cems.frontend.utils.LocalStorage;
//import javafx.fxml.FXML;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.layout.VBox;
//import com.cems.frontend.view.SceneNavigator;
//
//public class SidebarController {
//
//    @FXML private VBox sidebarRoot;
//    @FXML private ToggleButton darkModeToggle;
//
//    // Add all sidebar buttons so you can highlight them later
//    @FXML private Button homeBtn;
//    @FXML private Button allEventsBtn;
//    @FXML private Button createEventBtn;
//    @FXML private Button usersBtn;
//    @FXML private Button settingsBtn;
//    @FXML private Button languageBtn;
//    @FXML private Button logoutButton;
//
//
//    @FXML
//    private void handleDarkMode() {
//        Scene scene = sidebarRoot.getScene();
//        if (scene == null) return;
//
//        if (darkModeToggle.isSelected()) {
//            darkModeToggle.setText("ON");
//            applyDarkMode(scene);
//        } else {
//            darkModeToggle.setText("OFF");
//            removeDarkMode(scene);
//        }
//    }
//
//    private void applyDarkMode(Scene scene) {
//        String darkTheme = getClass()
//                .getResource("/com/cems/frontend/view/css/dark-theme.css")
//                .toExternalForm();
//
//        if (!scene.getStylesheets().contains(darkTheme)) {
//            scene.getStylesheets().add(darkTheme);
//        }
//    }
//
//    private void removeDarkMode(Scene scene) {
//        String darkTheme = getClass()
//                .getResource("/com/cems/frontend/view/css/dark-theme.css")
//                .toExternalForm();
//
//        scene.getStylesheets().remove(darkTheme);
//    }
//
//
//
//    // -------------------------
//    // Navigation
//    // -------------------------
//
//    @FXML
//    private void goToHome() {
//        SceneNavigator.loadPage("MainHome.fxml");
//        highlight(homeBtn);
//    }
//
//    @FXML
//    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
//        highlight(allEventsBtn);
//    }
//
//    @FXML
//    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
//        highlight(createEventBtn);
//    }
//
//    @FXML
//    private void goToSettings() {
//        SceneNavigator.loadPage("UserSettings.fxml");
//        highlight(settingsBtn);
//    }
//
//    @FXML
//    private void handleLogout() {
//        LocalStorage.set("token", "");
//        SceneNavigator.loadPage("login-view.fxml");
//    }
//
//    // -------------------------
//    // Highlight active button
//    // -------------------------
//
//    private void highlight(Button active) {
//        resetAll();
//        active.getStyleClass().add("active");
//    }
//
//
//
//
//    private void resetAll() {
//        reset(homeBtn);
//        reset(allEventsBtn);
//        reset(createEventBtn);
//        reset(usersBtn);
//        reset(settingsBtn);
//        reset(languageBtn);
//    }
//
//    private void reset(Button btn) {
//        btn.getStyleClass().remove("active");
//    }
//}
package com.cems.frontend.controllers.components;

import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.utils.SideBarState;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import com.cems.frontend.view.SceneNavigator;

public class SidebarController {

    @FXML private VBox sidebarRoot;
    @FXML private ToggleButton darkModeToggle;

    @FXML private Button homeBtn;
    @FXML private Button allEventsBtn;
    @FXML private Button createEventBtn;
    @FXML private Button usersBtn;
    @FXML private Button settingsBtn;
    @FXML private Button languageBtn;
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {

        SideBarState.set(this);
    }
    @FXML
    private void handleDarkMode() {
        Scene scene = sidebarRoot.getScene();
        if (scene == null) return;

        if (darkModeToggle.isSelected()) {
            darkModeToggle.setText("ON");
            applyDarkMode(scene);
        } else {
            darkModeToggle.setText("OFF");
            removeDarkMode(scene);
        }
    }

    private void applyDarkMode(Scene scene) {
        String darkTheme = getClass()
                .getResource("/com/cems/frontend/view/css/dark-theme.css")
                .toExternalForm();

        if (!scene.getStylesheets().contains(darkTheme)) {
            scene.getStylesheets().add(darkTheme);
        }
    }

    private void removeDarkMode(Scene scene) {
        String darkTheme = getClass()
                .getResource("/com/cems/frontend/view/css/dark-theme.css")
                .toExternalForm();

        scene.getStylesheets().remove(darkTheme);
    }



    // Navigation


    @FXML
    private void goToHome() {
        SceneNavigator.loadPage("MainHome.fxml");
    }

    @FXML
    private void goToAllEvents() {
        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void goToCreateEvent() {
        SceneNavigator.loadPage("create-event-view.fxml");
    }

    @FXML
    private void goToSettings() {
        SceneNavigator.loadPage("UserSettings.fxml");
    }

    @FXML
    private void handleLogout() {
        LocalStorage.set("token", "");
        SceneNavigator.loadPage("login-view.fxml");
    }


    // Highlighting active button


    public void setActive(String page) {
        resetAll();

        switch (page) {
            case "home" -> homeBtn.getStyleClass().add("active");
            case "events" -> allEventsBtn.getStyleClass().add("active");
            case "create" -> createEventBtn.getStyleClass().add("active");
            case "users" -> usersBtn.getStyleClass().add("active");
            case "settings" -> settingsBtn.getStyleClass().add("active");
        }
    }

    private void resetAll() {
        reset(homeBtn);
        reset(allEventsBtn);
        reset(createEventBtn);
        reset(usersBtn);
        reset(settingsBtn);
        reset(languageBtn);
    }

    private void reset(Button btn) {
        btn.getStyleClass().remove("active");
    }
}
