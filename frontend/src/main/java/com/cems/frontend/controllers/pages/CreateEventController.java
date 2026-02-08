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
    private final IEventService eventService = new ApiEventService();

    @FXML
    private void handleSubmit() {
        try {
            EventRequestDTO dto = eventFormController.getFormData();
            eventService.createEvent(dto);
            AlertHelper.showInfo("Success", "Event created successfully!");
            SceneNavigator.loadPage("home-view.fxml");

        } catch (NumberFormatException e) {
            AlertHelper.showError("Input Error", "Capacity must be a valid number.");
        } catch (IllegalArgumentException e) {
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