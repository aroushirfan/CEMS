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

    /**
     * Initializes the view using the Model passed from the Navigator.
     */
    public void initData(Event event) {
        this.currentEvent = event;

        // 1. Clear any previous bindings to prevent memory leaks
        titleLabel.textProperty().unbind();
        capacityLabel.textProperty().unbind();

        // 2. Bind properties so labels update automatically when the model changes
        titleLabel.textProperty().bind(currentEvent.titleProperty());
        capacityLabel.textProperty().bind(currentEvent.capacityProperty().asString("Capacity: %d"));

        // 3. Set labels for non-property fields
        updateStaticLabels();

        // 4. Trigger background fetch for the most recent server state
        refreshEventFromServer();
    }

    /**
     * Fetches fresh data using getEventById and updates the existing model.
     */
    private void refreshEventFromServer() {
        new Thread(() -> {
            try {
                // Fetch the latest state (Service handles DTO -> Model mapping)
                Event freshData = eventService.getEventById(currentEvent.getId().toString());

                // Update the model properties on the UI thread
                Platform.runLater(() -> {
                    currentEvent.setTitle(freshData.getTitle());
                    currentEvent.setDescription(freshData.getDescription());
                    currentEvent.setLocation(freshData.getLocation());
                    currentEvent.setCapacity(freshData.getCapacity());
                    currentEvent.setDateTime(freshData.getDateTime());

                    // Manual update for non-bound labels
                    updateStaticLabels();
                });
            } catch (Exception e) {
                // Silently log or handle if the event was deleted during navigation
                System.err.println("Background refresh failed: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Updates labels that are not directly bound to ObservableProperties.
     */
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
        // Correctly passes the Model to the navigator
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