package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventFormController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.EventDto.EventRequestDTO;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

/**
 * Controller for the Edit Event page. It manages the event editing process,
 * including form data handling and API communication.
 */
public class EditEventController {

  @FXML
  private EventFormController eventFormController;
  private final ApiEventService eventService = new ApiEventService();
  private Event eventModel;
  private ResourceBundle resourceBundle;

  /**
   * Initializes the controller with the event data to be edited. It sets up the form
   * with the existing event details.
   *
   * @param event The event to be edited.
   */
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
      AlertHelper.showInfo(resourceBundle.getString("eventForm.success_title"),
          resourceBundle.getString("eventForm.update_success_message"));
      SceneNavigator.loadContent(Paths.ALL_EVENTS);
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      AlertHelper.showError(resourceBundle.getString("eventForm.update_fail_title"),
          e.getMessage());
    }
  }

  @FXML
  private void handleCancel() {
    SceneNavigator.loadEventDetail(eventModel);
  }
}