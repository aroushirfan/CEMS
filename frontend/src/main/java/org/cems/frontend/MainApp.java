package org.cems.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Point to the fxml folder in your resources
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("CEMS - Campus Event Management System");
        stage.setScene(scene);
        stage.show();
    }
}
