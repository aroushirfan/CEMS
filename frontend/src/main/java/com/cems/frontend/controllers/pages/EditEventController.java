package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventFormController;
import com.cems.frontend.models.Event; // Using the frontend model
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.EventDto.EventRequestDTO;
// Removed EventResponseDTO import as it's now restricted to the Service
import javafx.fxml.FXML;

public class EditEventController {

    @FXML private EventFormController eventFormController;
    private final ApiEventService eventService = new ApiEventService();
    private Event eventModel;

    public void initData(Event event) {
        this.eventModel = event;
        eventFormController.setFormData(event);
    }

    @FXML
    private void handleUpdate() {
        try {
            EventRequestDTO updatedDto = eventFormController.getFormData();
            eventService.updateEvent(eventModel.getId().toString(), updatedDto);
            AlertHelper.showInfo("Success", "Event updated successfully!");
            SceneNavigator.loadPage("home-view.fxml");
        } catch (Exception e) {
            AlertHelper.showError("Update Failed", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.loadEventDetail(eventModel);
    }
}