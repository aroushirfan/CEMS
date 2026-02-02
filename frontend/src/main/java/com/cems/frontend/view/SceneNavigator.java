package com.cems.frontend.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneNavigator {
    private static Stage mainStage;

    // Sets the primary stage from MainApp
    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    // Navigates to a new page in the resources/view/pages folder
    public static void loadPage(String fxmlPath) {
        try {
            // 1. Get the resource URL
            URL resource = SceneNavigator.class.getResource("/com/cems/frontend/view/pages/" + fxmlPath);

            // 2. Safety check: prevent "Location is not set" error
            if (resource == null) {
                System.err.println("Error: Could not find FXML file at: /com/cems/frontend/view/pages/" + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load());
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Use Alert utility here later
        }
    }
}