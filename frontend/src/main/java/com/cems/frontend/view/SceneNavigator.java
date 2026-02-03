package com.cems.frontend.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneNavigator {
    private static Stage mainStage;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void loadPage(String fxmlPath) {
        try {
            URL resource = SceneNavigator.class.getResource("/com/cems/frontend/view/pages/" + fxmlPath);

            if (resource == null) {
                System.err.println("Error: Could not find FXML file at: /com/cems/frontend/view/pages/" + fxmlPath);
                return;
            }

            double width = mainStage.getWidth();
            double height = mainStage.getHeight();

            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load(), width, height);

            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}