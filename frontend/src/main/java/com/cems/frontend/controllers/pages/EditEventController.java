package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventFormController;
import com.cems.frontend.models.Event; // Using the frontend model
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.EventDto.EventRequestDTO;
// Removed EventResponseDTO import as it's now restricted to the Service
import javafx.fxml.FXML;

import java.util.ResourceBundle;

public class EditEventController {

    @FXML private EventFormController eventFormController;
    private final ApiEventService eventService = new ApiEventService();
    private Event eventModel;
    private ResourceBundle resourceBundle;

    public void initData(Event event) {
        resourceBundle = LocaleUtil.getInstance().getBundle(Paths.EDIT_VIEW);
        this.eventModel = event;
        eventFormController.setFormData(event);
    }

    @FXML
    private void handleUpdate() {
        try {
            EventRequestDTO updatedDto = eventFormController.getFormData();
            eventService.updateEvent(eventModel.getId().toString(), updatedDto);
            AlertHelper.showInfo(resourceBundle.getString("eventForm.success_title"), resourceBundle.getString("eventForm.update_success_message"));
            SceneNavigator.loadContent(Paths.ALL_EVENTS);
        } catch (Exception e) {
            AlertHelper.showError(resourceBundle.getString("eventForm.update_fail_title"), e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.loadEventDetail(eventModel);
    }
}