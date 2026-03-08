package com.cems.frontend;

import com.cems.frontend.utils.LocalStorage;
import javafx.application.Application;
import javafx.stage.Stage;
import com.cems.frontend.view.SceneNavigator;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        LocalStorage.set("token", "");
        LocalStorage.set("role", "");
        SceneNavigator.setStage(stage);
        stage.setTitle("CEMS - Campus Event Management System");
        //SceneNavigator.loadPage("admin-page.fxml");
        SceneNavigator.loadPage("home-view.fxml");

        //SceneNavigator.loadPage("UserSettings.fxml");
////        SceneNavigator.loadPage("Signup.fxml");
////        SceneNavigator.loadPage("Login.fxml");
//        SceneNavigator.loadPage("MainHome.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}