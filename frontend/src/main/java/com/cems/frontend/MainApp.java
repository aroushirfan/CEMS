package com.cems.frontend;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cems.frontend.view.SceneNavigator;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneNavigator.setStage(stage);
        stage.setTitle("CEMS - Campus Event Management System");
//        SceneNavigator.loadPage("home-view.fxml");

        SceneNavigator.loadPage("Attendance.fxml");
//        SceneNavigator.loadPage("UserSettings.fxml");
//        SceneNavigator.loadPage("Signup.fxml");
//        SceneNavigator.loadPage("Login.fxml");
//        SceneNavigator.loadPage("MainHome.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}