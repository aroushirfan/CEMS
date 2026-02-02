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
import java.time.LocalDate;

public class UserSettingsController {

    // Profile section
    @FXML
    private ImageView profileImage;

    @FXML
    private Button editProfileButton;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private DatePicker dobPicker;

    // Contact info
    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Button saveButton;

    /**
     * Called automatically after FXML is loaded
     */
    @FXML
    public void initialize() {
        // Load default profile image
        loadDefaultProfileImage();

        // Populate language dropdown
        languageComboBox.getItems().addAll(
                "English",
                "Finnish",
                "Spanish"
        );

        // Mock user data (temporary)
        fullNameField.setText("Jamie Campbell");
        usernameField.setText("jamie123");
        dobPicker.setValue(LocalDate.of(2005, 12, 3));
        phoneField.setText("+358 400 123456");
        emailField.setText("jamie@example.com");
        languageComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Open file chooser and update profile image
     */
    @FXML
    private void handleEditProfileImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        Stage stage = (Stage) profileImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            profileImage.setImage(
                    new Image(selectedFile.toURI().toString())
            );
        }
    }

    /**
     * Save button handler (mock for now)
     */
    @FXML
    private void handleSaveChanges() {
        String fullName = fullNameField.getText();
        String username = usernameField.getText();
        LocalDate dob = dobPicker.getValue();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String language = languageComboBox.getValue();

        System.out.println("Saving user settings:");
        System.out.println("Name: " + fullName);
        System.out.println("Username: " + username);
        System.out.println("DOB: " + dob);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Language: " + language);

        // TODO: Replace with real service call
    }

    /**
     * Loads a default profile image from resources
     */
    private void loadDefaultProfileImage() {
        Image image = new Image(
                getClass().getResource(
                        "/org/cems/frontend/view/assets/profile.jpeg"
                ).toExternalForm()
        );
        profileImage.setImage(image);

}

}

