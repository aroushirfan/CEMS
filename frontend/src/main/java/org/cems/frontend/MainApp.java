package org.cems.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.cems.frontend.view.SceneNavigator;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 1. Initialize the navigator with the primary stage
        SceneNavigator.setStage(stage);

        // 2. Set the window title
        stage.setTitle("CEMS - Campus Event Management System");

        // 3. Use the navigator to load your first page from resources/view/pages/
//        SceneNavigator.loadPage("home-view.fxml");
//        SceneNavigator.loadPage("UserSettings.fxml");
        SceneNavigator.loadPage("Signup.fxml");
//        SceneNavigator.loadPage("Login.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
