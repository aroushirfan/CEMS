package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.AttendanceService;
import com.cems.frontend.services.RsvpService;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.utils.RbacUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class EventDetailController {

    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label capacityLabel;
    @FXML private Button registerNowButton;
    @FXML private Button viewAttendanceButton;
    @FXML private Button checkInButton;
    @FXML private HBox buttonLayout;

    private final BooleanProperty registered = new SimpleBooleanProperty(false);
    private final ApiEventService eventService = new ApiEventService();
    private Event currentEvent; // The property-based Frontend Model

    private final RsvpService rsvpService = new RsvpService(LocalHttpClientHelper.getClient(),LocalHttpClientHelper.getMapper());
    private final AttendanceService attendanceService = new AttendanceService(LocalHttpClientHelper.getClient(),LocalHttpClientHelper.getMapper());

    @FXML
    public void initialize() {
        // Bind button text to the registered property
        registerNowButton.textProperty().bind(
                registered
                        .map(isRegistered -> isRegistered ? "Cancel Registration" : "Register Now")
        );
        buttonLayout.getChildren().clear();
    }

    public void initData(Event event) {
        this.currentEvent = event;
        titleLabel.textProperty().unbind();
        capacityLabel.textProperty().unbind();
        titleLabel.textProperty().bind(currentEvent.titleProperty());
        capacityLabel.textProperty().bind(currentEvent.capacityProperty().asString("Capacity: %d"));
        getRegisteredEvents();
        checkAttendanceStatus();
        updateStaticLabels();
        refreshEventFromServer();

        boolean isUser = RbacUtil.isUser();
        boolean eventPending = Instant.now().isBefore(currentEvent.getDateTime());
        boolean canCheckIn = isUser && !eventPending;
        boolean canRegister = isUser && eventPending;


        //  Display button only if it is a user and the event has not started
        if (canRegister) {
            buttonLayout.getChildren().add(registerNowButton);
        }
        //  Display button if user is not normal user
//        if (RbacUtil.isFaculty() || RbacUtil.isAdmin()) {
            buttonLayout.getChildren().add(viewAttendanceButton);
//        }
        //  Display button if event has started and is normal user
        if (canCheckIn) {
            buttonLayout.getChildren().add(checkInButton);
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
                System.err.println("Background refresh failed: " + e.getMessage());
            }
        }).start();
    }

    private void updateStaticLabels() {
        locationLabel.setText(currentEvent.getLocation() != null ? currentEvent.getLocation() : "TBD");
        descriptionLabel.setText(currentEvent.getDescription() != null ? currentEvent.getDescription() : "No description provided.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy - HH:mm");
        if (currentEvent.getDateTime() != null) {
            dateLabel.setText(currentEvent.getDateTime().atZone(ZoneId.systemDefault()).format(formatter));
        }
    }

    private void getRegisteredEvents(){
        Task<List<Event>> rsvpTask = new Task<>() {
            @Override
            protected List<Event> call() throws Exception {
                return rsvpService.getRegisteredEvents();
            }
        };

        rsvpTask.setOnSucceeded(e -> {
            registered.set(rsvpTask.getValue().stream()
                    .anyMatch(event -> event.getId().equals(currentEvent.getId())));
        });

        //      run the thread to check rsvp in the background
        new Thread(rsvpTask).start();
    }

    private void checkAttendanceStatus(){
        Task<Boolean> checkInStatusTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return attendanceService.hasCheckedIn(currentEvent.getId());
            }
        };

        checkInStatusTask.setOnSucceeded(e -> {
            Platform.runLater(() -> checkInButton.setDisable(checkInStatusTask.getValue()));
        });
        //      run the thread to check rsvp in the background
        new Thread(checkInStatusTask).start();
    }

    @FXML
    private void handleCheckIn(){
        Task<String> checkInTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                return attendanceService.checkInEvent(currentEvent.getId());
            }
        };

        checkInTask.setOnSucceeded(e -> {
            Platform.runLater(()->checkInSuccess(checkInTask.getValue()));
        });

        checkInTask.setOnFailed(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Check In Failed. Try again.");
            alert.show();
        });
        //      run the thread to send check in the background
        new Thread(checkInTask).start();
    }

    private void checkInSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRegisterNow() {
        Task<String> rsvpTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                if (registered.get()){
                    return rsvpService.cancelRegistration(currentEvent.getId());
                }
                return rsvpService.register(currentEvent.getId());
            }
        };

        rsvpTask.setOnSucceeded(e -> {
            Platform.runLater(()->handleRsvpState(rsvpTask.getValue()));
        });

        rsvpTask.setOnFailed(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Registration Failed. Try again.");
            alert.show();
        });
        //      run the thread to send rsvp in the background
        new Thread(rsvpTask).start();
    }

    private void handleRsvpState(String message){
        registered.set(!registered.get());
        rsvpSuccess(message);
    }

    private void rsvpSuccess(String message){
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
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete '" + currentEvent.getTitle() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                eventService.deleteEvent(currentEvent.getId().toString());
                AlertHelper.showInfo("Deleted", "Event removed successfully.");
//                SceneNavigator.loadPage("home-view.fxml");
                NavigationNotifier.getInstance().notifyAllObservers(Paths.HOME);
            } catch (Exception e) {
                AlertHelper.showError("Error", "Could not delete: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
    }
}