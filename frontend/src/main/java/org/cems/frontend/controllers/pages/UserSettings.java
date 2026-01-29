package org.cems.frontend.controllers.pages;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UserSettingsController {

    // Profile Image + Edit
    @FXML private ImageView profileImage;
    @FXML private Button editProfileButton;

    // Profile Info
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private DatePicker dobPicker;

    // Contact Info
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> languageComboBox;

    // Save button
    @FXML private Button saveButton;
    }


    @FXML
    public void initialize() {
        // Populate language dropdown
        languageComboBox.getItems().addAll("English", "Finnish", "Spanish");

        // Optional: Load default user data (mock)
        fullNameField.setText("Jane");
        usernameField.setText("jane123");
        dobPicker.setValue(java.time.LocalDate.of(2005, 12, 3));
        phoneField.setText("+358 400 123456");
        emailField.setText("jane@example.com");
        languageComboBox.getSelectionModel().selectFirst();

        // Edit profile image button
        editProfileButton.setOnAction(e -> handleEditProfileImage());

        // Save changes button
        saveButton.setOnAction(e -> handleSaveChanges());
    }

    private void handleEditProfileImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) profileImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void handleSaveChanges() {
        // Here you can collect all field values and save them
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        java.time.LocalDate dob = dobPicker.getValue();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String language = languageComboBox.getValue();

        System.out.println("Saving changes:");
        System.out.println(fullName + ", " + username + ", " + dob + ", " + phone + ", " + email + ", " + language);

        // TODO: Call your service to actually save data
    }
}
