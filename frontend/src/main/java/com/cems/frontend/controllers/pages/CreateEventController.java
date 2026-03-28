package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventFormController;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.EventDto.EventRequestDTO;
import javafx.fxml.FXML;

import java.util.ResourceBundle;

public class CreateEventController {

    @FXML private EventFormController eventFormController;
    private final IEventService eventService = new ApiEventService();
    private ResourceBundle resourceBundle;

    @FXML
    public void initialize(){
        resourceBundle = LocaleUtil.getInstance().getBundle(Paths.CREATE_EVENT);
    }

    @FXML
    private void handleSubmit() {
        try {
            EventRequestDTO dto = eventFormController.getFormData();
            eventService.createEvent(dto);
            AlertHelper.showInfo(resourceBundle.getString("eventForm.success_title"), resourceBundle.getString("eventForm.create_success_message"));
            SceneNavigator.loadContent(Paths.CREATE_EVENT);

        } catch (NumberFormatException e) {
            AlertHelper.showError(resourceBundle.getString("eventForm.input_error"), resourceBundle.getString("eventForm.invalid_capacity"));
        } catch (IllegalArgumentException e) {
            AlertHelper.showError(resourceBundle.getString("eventForm.missing_info"), e.getMessage());
        } catch (Exception e) {
            AlertHelper.showError(resourceBundle.getString("eventForm.server_error"), resourceBundle.getString("eventForm.create_error_message") + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneNavigator.loadContent(Paths.CREATE_EVENT);
    }
}