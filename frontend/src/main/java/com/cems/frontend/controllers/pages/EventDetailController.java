package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EventDetailController {

    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label capacityLabel;

    private final ApiEventService eventService = new ApiEventService();
    private Event currentEvent; // The property-based Frontend Model

    public void initData(Event event) {
        this.currentEvent = event;
        titleLabel.textProperty().unbind();
        capacityLabel.textProperty().unbind();
        titleLabel.textProperty().bind(currentEvent.titleProperty());
        capacityLabel.textProperty().bind(currentEvent.capacityProperty().asString("Capacity: %d"));

        updateStaticLabels();
        refreshEventFromServer();
    }

    private void refreshEventFromServer() {
        new Thread(() -> {
            try {
                Event freshData = eventService.getEventById(currentEvent.getId().toString());

                Platform.runLater(() -> {
                    currentEvent.setTitle(freshData.getTitle());
                    currentEvent.setDescription(freshData.getDescription());
                    currentEvent.setLocation(freshData.getLocation());
                    currentEvent.setCapacity(freshData.getCapacity());
                    currentEvent.setDateTime(freshData.getDateTime());

                    updateStaticLabels();
                });
            } catch (Exception e) {
                System.err.println("Background refresh failed: " + e.getMessage());
            }
        }).start();
    }

    private void updateStaticLabels() {
        locationLabel.setText(currentEvent.getLocation() != null ? currentEvent.getLocation() : "TBD");
        descriptionLabel.setText(currentEvent.getDescription() != null ? currentEvent.getDescription() : "No description provided.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy - HH:mm");
        if (currentEvent.getDateTime() != null) {
            dateLabel.setText(currentEvent.getDateTime().atZone(ZoneId.systemDefault()).format(formatter));
        }
    }

    @FXML
    private void handleEdit() {
        SceneNavigator.loadEditPage(currentEvent);
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete '" + currentEvent.getTitle() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                eventService.deleteEvent(currentEvent.getId().toString());
                AlertHelper.showInfo("Deleted", "Event removed successfully.");
                SceneNavigator.loadPage("home-view.fxml");
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack() {
        SceneNavigator.loadPage("home-view.fxml");
    }
}