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
            eventService.createEvent(dto); // Backend POST call

            AlertHelper.showInfo("Success", "Event created successfully!");
            SceneNavigator.loadPage("home-view.fxml");
        } catch (NumberFormatException e) {
            AlertHelper.showError("Input Error", "Capacity must be a number.");
        } catch (Exception e) {
            AlertHelper.showError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.loadPage("home-view.fxml");
    }
}