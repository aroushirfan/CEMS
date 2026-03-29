package com.cems.frontend.controllers.components;

import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.RsvpService;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.utils.RbacUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EventCardController {
    @FXML private Label titleLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label spotsLabel;
    @FXML private Button registerButton;

    private Event currentEvent;
    private final BooleanProperty registered = new SimpleBooleanProperty(false);
    private final RsvpService rsvpService = new RsvpService(LocalHttpClientHelper.getClient(),LocalHttpClientHelper.getMapper());
    private ResourceBundle rb;
    @FXML private Button learnMoreButton;

    @FXML
    public void initialize() {
        rb = LocaleUtil.getInstance().getBundle(Paths.EVENT_DETAIL_VIEW);
        learnMoreButton.setText(rb.getString("eventDetail.learn_more"));
    }

    public void setEventModel(Event event) {
        this.currentEvent = event;
        titleLabel.textProperty().bind(event.titleProperty());
        if (event.getDateTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());
            dateTimeLabel.setText(formatter.format(event.getDateTime()));
        }
        locationLabel.setText(event.getLocation() != null ? event.getLocation() : "TBD");
        spotsLabel.textProperty().bind(event.capacityProperty().asString("Capacity: %d"));
        registerButton.textProperty().bind(
                registered
                        .map(isRegistered -> isRegistered ? rb.getString("eventDetail.cancel_register") : rb.getString("eventDetail.register"))
        );
        getRegisteredEvents();
        if (Instant.now().isBefore(currentEvent.getDateTime()) && RbacUtil.isUser()) {
            registerButton.setDisable(false);
        }
    }

    private void getRegisteredEvents(){
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
    }

    @FXML
    private void handleRegister() {
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
            alert.setContentText(rsvpTask.getException().getMessage());
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

    public Button getLearnMoreButton() {
        return learnMoreButton;
    }
}