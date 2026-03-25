package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.NavigationObserver;
import com.cems.frontend.models.Paths;
import com.cems.frontend.view.SceneNavigator;
import javafx.application.Platform;
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
        FXMLLoader loader = SceneNavigator.getLoader(getClass().getResource(Paths.SIDEBAR.path));
        VBox sidebar = loader.load();
        sidebarController = loader.getController();

        borderPane.setLeft(sidebar);
        FXMLLoader contentLoader = SceneNavigator.getLoader(getClass().getResource(Paths.HOME.path));
        borderPane.setCenter(contentLoader.load());

        NavigationNotifier.getInstance().addObserver(this);
    }

    @Override
    @Deprecated
    public void setPage(Paths page) {
    }

    public Object setPageReturnController(Paths page) {
        try {
            FXMLLoader loader = SceneNavigator.getLoader(getClass().getResource(page.path));
            borderPane.setCenter(loader.load());
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
