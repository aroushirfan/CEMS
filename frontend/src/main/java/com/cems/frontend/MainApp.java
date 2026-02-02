package com.cems.frontend;

import javafx.application.Application;
import javafx.stage.Stage;
import com.cems.frontend.view.SceneNavigator;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // 1. Initialize the navigator with the primary stage
        SceneNavigator.setStage(stage);

        // 2. Set the window title
        stage.setTitle("CEMS - Campus Event Management System");

        // 3. Use the navigator to load your first page from resources/view/pages/
        SceneNavigator.loadPage("home-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
