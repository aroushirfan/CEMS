package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventFormController;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.EventDto.EventRequestDTO;
import javafx.fxml.FXML;

public class CreateEventController {

    @FXML private EventFormController eventFormController;

    // Best Practice: Use the Interface type for the service
    private final IEventService eventService = new ApiEventService();

    @FXML
    private void handleSubmit() {
        try {
            // 1. Get the Request DTO from the form
            EventRequestDTO dto = eventFormController.getFormData();

            // 2. Send the DTO to the backend via the service
            eventService.createEvent(dto);

            // 3. Success feedback and navigation
            AlertHelper.showInfo("Success", "Event created successfully!");
            SceneNavigator.loadPage("home-view.fxml");

        } catch (NumberFormatException e) {
            // Catches errors if capacityField.getText() isn't a valid long
            AlertHelper.showError("Input Error", "Capacity must be a valid number.");
        } catch (IllegalArgumentException e) {
            // Catches validation errors from getFormData (like missing date)
            AlertHelper.showError("Missing Information", e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError("Server Error", "Could not create event: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.loadPage("home-view.fxml");
    }
}