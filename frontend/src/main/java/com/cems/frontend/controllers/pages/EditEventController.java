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

    /**
     * Best Practice: Accepts the Model directly from SceneNavigator.
     * No DTO mapping happens here anymore because it was handled in the Service.
     */
    public void initData(Event event) {
        this.eventModel = event;
        // Pass the model directly to the form
        eventFormController.setFormData(event);
    }

    @FXML
    private void handleUpdate() {
        try {
            // getFormData() still returns the DTO needed for the API PUT request
            EventRequestDTO updatedDto = eventFormController.getFormData();

            // Call service using the ID from the model
            eventService.updateEvent(eventModel.getId().toString(), updatedDto);

            AlertHelper.showInfo("Success", "Event updated successfully!");
            SceneNavigator.loadPage("home-view.fxml");
        } catch (Exception e) {
            AlertHelper.showError("Update Failed", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        // Return to the detail view for this specific event model
        SceneNavigator.loadEventDetail(eventModel);
    }
}