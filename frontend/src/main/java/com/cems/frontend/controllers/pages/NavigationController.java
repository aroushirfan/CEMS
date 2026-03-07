package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.NavigationObserver;
import com.cems.frontend.models.Paths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class NavigationController implements NavigationObserver {
    @FXML
    private BorderPane borderPane;
    private SidebarController sidebarController;

    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.SIDEBAR.path));
        VBox sidebar = loader.load();
        sidebarController = loader.getController();

        borderPane.setLeft(sidebar);
        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource(Paths.HOME.path));
        borderPane.setCenter(contentLoader.load());

        NavigationNotifier.getInstance().addObserver(this);
    }

    @Override
    public void setPage(Paths page) {
        System.out.println("Loading page");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page.path));
            borderPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
