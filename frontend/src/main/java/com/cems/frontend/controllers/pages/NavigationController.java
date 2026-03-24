package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.NavigationObserver;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocaleUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NavigationController implements NavigationObserver {
    @FXML
    private BorderPane borderPane;
    private SidebarController sidebarController;

    @FXML
    public void initialize() throws IOException {
        //   localization
        Locale locale = LocaleUtil.getInstance().getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Bundles", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.SIDEBAR.path),resourceBundle);
        VBox sidebar = loader.load();
        sidebarController = loader.getController();

        borderPane.setLeft(sidebar);
        FXMLLoader contentLoader = new FXMLLoader(getClass().getResource(Paths.HOME.path),resourceBundle);
        borderPane.setCenter(contentLoader.load());

        NavigationNotifier.getInstance().addObserver(this);
    }

    @Override
    @Deprecated
    public void setPage(Paths page) {
    }

    public Object setPageReturnController(Paths page) {
        try {
            Locale locale = LocaleUtil.getInstance().getLocale();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("Bundles", locale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(page.path),resourceBundle);
            borderPane.setCenter(loader.load());
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
