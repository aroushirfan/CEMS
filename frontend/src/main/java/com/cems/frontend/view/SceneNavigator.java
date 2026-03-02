package com.cems.frontend.view;

import com.cems.frontend.controllers.pages.EditEventController;
import com.cems.frontend.controllers.pages.EventDetailController;

import com.cems.frontend.models.Event; // Import your property-based model
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

    public static void loadEventDetail(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/cems/frontend/view/pages/event-detail-view.fxml"));
            Scene scene = new Scene(loader.load(), mainStage.getWidth(), mainStage.getHeight());
            EventDetailController controller = loader.getController();
            controller.initData(event);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadEditPage(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/cems/frontend/view/pages/edit-event-view.fxml"));

            Scene scene = new Scene(loader.load(), mainStage.getWidth(), mainStage.getHeight());

            EditEventController controller = loader.getController();
            controller.initData(event);
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}