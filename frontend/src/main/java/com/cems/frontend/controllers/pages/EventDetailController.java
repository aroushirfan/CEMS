package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.AttendanceService;
import com.cems.frontend.services.RsvpService;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.utils.RbacUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Controller for the Event Detail view. It manages the display
 * of event information and user interactions such as
 * registering for an event, checking in, and viewing attendance.
 * The controller also handles role-based access control for certain
 * actions and updates the UI based on the user's registration and check-in status.
 */
public class EventDetailController {

  @FXML
  private Label titleLabel;
  @FXML
  private Label dateLabel;
  @FXML
  private Label locationLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private Label capacityLabel;
  @FXML
  private Button registerNowButton;
  @FXML
  private Button viewAttendanceButton;
  @FXML
  private Button checkInButton;
  @FXML
  private HBox buttonLayout;

  private final BooleanProperty registered = new SimpleBooleanProperty(false);
  private final BooleanProperty checkedIn = new SimpleBooleanProperty(false);
  private final ApiEventService eventService = new ApiEventService();
  private Event currentEvent; // The property-based Frontend Model

  private final RsvpService rsvpService = new RsvpService(LocalHttpClientHelper.getClient(),
      LocalHttpClientHelper.getMapper());
  private final AttendanceService attendanceService = new AttendanceService(
      LocalHttpClientHelper.getClient(), LocalHttpClientHelper.getMapper());
  private LocaleUtil localeService = LocaleUtil.getInstance();
  private ResourceBundle rb;

  /**
   * Initializes the controller. This method is called after the FXML
   * fields have been injected. It sets up the initial state of the UI,
   * binds button text and disable properties to the user's registration
   * and check-in status, and loads the appropriate buttons based on the user's role.
   */
  @FXML
  public void initialize() {
    rb = localeService.getBundle(Paths.EVENT_DETAIL_VIEW);
    // Bind button text to the registered property
    registerNowButton.textProperty().bind(
        registered
            .map(isRegistered -> isRegistered
                ? rb.getString("eventDetail.cancel_register")
                : rb.getString("eventDetail.register"))
    );
    // Bind button disable to the checkin status
    checkInButton.disableProperty().bind(registered.not());
    checkInButton.textProperty().bind(
        checkedIn
            .map(isCheckedIn -> isCheckedIn
                ? rb.getString("eventDetail.attended")
                : rb.getString("eventDetail.check_in"))
    );
    buttonLayout.getChildren().remove(viewAttendanceButton);
  }

  /**
   * Initializes the event data for the detail view. This method is called
   * when navigating to the event detail page. It binds the event properties
   * to the UI labels, checks the user's RSVP and check-in status, and updates
   * the UI based on the user's role and the event's date.
   *
   * @param event The event to display in detail.
   */
  public void initData(Event event) {
    this.currentEvent = event;
    titleLabel.textProperty().unbind();
    capacityLabel.textProperty().unbind();
    titleLabel.textProperty().bind(currentEvent.titleProperty());
    capacityLabel.textProperty().bind(currentEvent.capacityProperty().asString("Capacity: %d"));
    checkUserEventRsvpStatus();
    updateStaticLabels();
    refreshEventFromServer();

    boolean isUser = RbacUtil.isUser();
    boolean eventPending = Instant.now().isBefore(currentEvent.getDateTime());
    boolean canRegister = isUser && eventPending;

    //  Display button only if it is a user and the event has not started
    if (canRegister) {
      registerNowButton.setDisable(false);
    }
    //  Display button if user is not normal user
    if (RbacUtil.isFaculty() || RbacUtil.isAdmin()) {
      buttonLayout.getChildren().add(viewAttendanceButton);
    }
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
        System.err.println("Background refresh failed: "
            + e.getMessage());
      }
    }).start();
  }

  private void updateStaticLabels() {
    locationLabel.setText(currentEvent.getLocation() != null
        ? currentEvent.getLocation()
        : "TBD");
    descriptionLabel.setText(currentEvent.getDescription() != null
        ? currentEvent.getDescription()
        : rb.getString("eventDetail.no_description"));

    DateTimeFormatter formatter = localeService.dateTime(FormatStyle.FULL, FormatStyle.SHORT);
    if (currentEvent.getDateTime() != null) {
      dateLabel.setText(currentEvent.getDateTime()
          .atZone(ZoneId.systemDefault()).format(formatter));
    }
  }

  private void checkUserEventRsvpStatus() {
    Task<Boolean> rsvpTask = new Task<>() {
      @Override
      protected Boolean call() throws Exception {
        return rsvpService.checkUserRsvp(currentEvent.getId());
      }
    };

    rsvpTask.setOnSucceeded(e -> {
      registered.set(rsvpTask.getValue());
    });
    //   run the thread to check rsvp in the background
    new Thread(rsvpTask).start();

    Task<Boolean> checkInStatusTask = new Task<>() {
      @Override
      protected Boolean call() throws Exception {
        return attendanceService.hasCheckedIn(currentEvent.getId());
      }
    };

    checkInStatusTask.setOnSucceeded(e -> {
      checkedIn.set(checkInStatusTask.getValue());
    });

    //   run the thread to check rsvp in the background
    new Thread(checkInStatusTask).start();
  }

  @FXML
  private void handleCheckIn() {
    Task<String> checkInTask = new Task<>() {
      @Override
      protected String call() throws Exception {
        return attendanceService.checkInEvent(currentEvent.getId());
      }
    };

    checkInTask.setOnSucceeded(e -> {
      Platform.runLater(() -> checkInSuccess(checkInTask.getValue()));
    });

    checkInTask.setOnFailed(e -> {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText(checkInTask.getException().getMessage());
      alert.show();
    });
    //      run the thread to send check in the background
    new Thread(checkInTask).start();
  }

  private void checkInSuccess(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(message);
    alert.showAndWait();
    checkInButton.setDisable(true);
  }

  @FXML
  private void handleRegisterNow() {
    Task<String> rsvpTask = new Task<>() {
      @Override
      protected String call() throws Exception {
        if (registered.get()) {
          return rsvpService.cancelRegistration(currentEvent.getId());
        }
        return rsvpService.register(currentEvent.getId());
      }
    };

    rsvpTask.setOnSucceeded(e -> {
      Platform.runLater(() -> handleRsvpState(rsvpTask.getValue()));
    });

    rsvpTask.setOnFailed(e -> {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText(rsvpTask.getException().getMessage());
      alert.show();
    });
    //      run the thread to send rsvp in the background
    new Thread(rsvpTask).start();
  }

  private void handleRsvpState(String message) {
    registered.set(!registered.get());
    rsvpSuccess(message);
  }

  private void rsvpSuccess(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @FXML
  private void handleViewAttendance() {
    SceneNavigator.loadAttendancePage(currentEvent);
  }

  @FXML
  private void handleEdit() {
    SceneNavigator.loadEditPage(currentEvent);
  }

  @FXML
  private void handleDelete() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(rb.getString("eventDetail.confirm_delete"));
    alert.setHeaderText(rb.getString("eventDetail.delete_event") + currentEvent.getTitle() + "'?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      try {
        eventService.deleteEvent(currentEvent.getId().toString());
        AlertHelper.showInfo(rb.getString("eventDetail.delete_success_title"),
            rb.getString("eventDetail.delete_success_message"));
        SceneNavigator.loadContent(Paths.HOME);
      } catch (Exception e) {
        AlertHelper.showError(rb.getString("eventDetail.delete_error"),
            rb.getString("eventDetail.delete_error_message") + e.getMessage());
      }
    }
  }

  @FXML
  private void handleBack() {
    SceneNavigator.loadContent(Paths.ALL_EVENTS);
  }
}