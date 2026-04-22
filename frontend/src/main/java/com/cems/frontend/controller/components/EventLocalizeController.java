package com.cems.frontend.controller.components;

import com.cems.frontend.models.Event;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.utils.Language;
import com.cems.shared.model.EventDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EventLocalizeController {
    @FXML
    private Button cancelButton;

    @FXML
    private TextField eventFormDescription;

    @FXML
    private TextField eventFormTitle;

    @FXML
    private ComboBox<Language> languageCombobox;

    @FXML
    private Button saveButton;

    @FXML
    private TextField eventFormLocation;

    private ApiEventService apiEventService;

    private Event selectedEvent;

    @FXML
    private void initialize() {
        apiEventService = new ApiEventService();


        languageCombobox.getItems().addAll(Language.getAllLanguages());
        languageCombobox.getSelectionModel().select(Language.EN);

        languageCombobox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (!oldVal.equals(newVal)) {
                onLanguageChange(newVal);
            }
        });

        saveButton.setOnAction(event -> onSave(languageCombobox.getSelectionModel().getSelectedItem()));
        cancelButton.setOnAction(this::onCancel);
        if (eventFormTitle.getText().isBlank()) {
            saveButton.setDisable(true);
        }

        eventFormTitle.textProperty().addListener((obs, oldVal, newVal) -> saveButton.setDisable(newVal.isBlank()));
    }

    public void setEventId(String id) {
        try {
            selectedEvent = apiEventService.getLocalEventById(id, Language.EN);
            eventFormTitle.setText(selectedEvent.getTitle());
            eventFormDescription.setText(selectedEvent.getDescription());
            eventFormLocation.setText(selectedEvent.getLocation());
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            showAlert(e);
        }
    }

    public void onLanguageChange(Language language) {
        try {
            selectedEvent = apiEventService.getLocalEventById(selectedEvent.getId().toString(), language);
            eventFormTitle.setText(selectedEvent.getTitle());
            eventFormDescription.setText(selectedEvent.getDescription());
            eventFormLocation.setText(selectedEvent.getLocation());
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            showAlert(e);
        }
    }

    private void onSave(Language language) {
        try {
            var data = new EventDto.EventLocalRequestDTO(eventFormTitle.getText(), eventFormDescription.getText().isBlank() ? null : eventFormDescription.getText(), eventFormLocation.getText().isBlank() ? null : eventFormLocation.getText());
            apiEventService.updateLocalEvent(selectedEvent.getId().toString(), data, language);
            closeWindow();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            showAlert(e);
        }
    }

    private void onCancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }


}
