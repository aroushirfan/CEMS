//package com.cems.frontend.controllers.pages;
//
//import com.cems.frontend.utils.SideBarState;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.File;
//import java.time.LocalDate;
//
//public class UserSettingsController {
//
//    // Profile section
//    @FXML
//    private ImageView profileImage;
//
//    @FXML
//    private Button editProfileButton;
//
//    @FXML
//    private TextField fullNameField;
//
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private DatePicker dobPicker;
//
//    // Contact info
//    @FXML
//    private TextField phoneField;
//
//    @FXML
//    private TextField emailField;
//
//    @FXML
//    private ComboBox<String> languageComboBox;
//
//    @FXML
//    private Button saveButton;
//
//
//    @FXML
//    public void initialize() {
//        // Load default profile image
//        loadDefaultProfileImage();
//
//
//        languageComboBox.getItems().addAll(
//                "English",
//                "Finnish"
//
//        );
//
//        // Mock user data (temporary)
//        fullNameField.setText("Jamie Campbell");
//        usernameField.setText("jamie123");
//        dobPicker.setValue(LocalDate.of(2005, 12, 3));
//        phoneField.setText("+358 400 123456");
//        emailField.setText("jamie@example.com");
//        languageComboBox.getSelectionModel().selectFirst();
//    }
//
//    /**
//     * Open file chooser and update profile image
//     */
//    @FXML
//    private void handleEditProfileImage() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select Profile Image");
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter(
//                        "Image Files", "*.png", "*.jpg", "*.jpeg"
//                )
//        );
//
//        Stage stage = (Stage) profileImage.getScene().getWindow();
//        File selectedFile = fileChooser.showOpenDialog(stage);
//
//        if (selectedFile != null) {
//            profileImage.setImage(
//                    new Image(selectedFile.toURI().toString())
//            );
//        }
//    }
//
//    /**
//     * Save button handler (mock for now)
//     */
//    @FXML
//    private void handleSaveChanges() {
//        String fullName = fullNameField.getText();
//        String username = usernameField.getText();
//        LocalDate dob = dobPicker.getValue();
//        String phone = phoneField.getText();
//        String email = emailField.getText();
//        String language = languageComboBox.getValue();
//
//        System.out.println("Saving user settings:");
//        System.out.println("Name: " + fullName);
//        System.out.println("Username: " + username);
//        System.out.println("DOB: " + dob);
//        System.out.println("Phone: " + phone);
//        System.out.println("Email: " + email);
//        System.out.println("Language: " + language);
//
//        // TODO: Replace with real service call
//    }
//
//    /**
//     * Loads a default profile image from resources
//     */
//    private void loadDefaultProfileImage() {
//        Image image = new Image(
//                getClass().getResource(
//                        "/com/cems/frontend/view/assets/profile.jpeg"
//                ).toExternalForm()
//        );
//        profileImage.setImage(image);
//
//}
//
//}

package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Paths;
import com.cems.frontend.models.User;
import com.cems.frontend.services.UserService;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ResourceBundle;

public class UserSettingsController {

    @FXML private ImageView profileImage;
    @FXML private TextField fullNameField;

    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Button saveButton;

    private final UserService userService = new UserService();
    private User currentUser;
    private String selectedProfileImagePath;
    private ResourceBundle resourceBundle;

    @FXML
    public void initialize() {
        resourceBundle = LocaleUtil.getInstance().getBundle(Paths.USER_SETTINGS);
        loadDefaultProfileImage();
        loadUserFromBackend();
    }

    private void loadUserFromBackend() {
        try {
            currentUser = userService.getCurrentUser();
            fillUIWithUser(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, resourceBundle.getString("userSettings.profile_error")).show();
        }
    }

    private void fillUIWithUser(User user) {
        // Full name
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "");
        fullNameField.setText(fullName.trim());

        phoneField.setText(user.getPhone());
        emailField.setText(user.getEmail());
        dobPicker.setValue(user.getDob());


        if (user.getProfileImageUrl() != null) {
            profileImage.setImage(new Image(user.getProfileImageUrl()));
        }
    }

    @FXML
    private void handleEditProfileImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(resourceBundle.getString("userSettings.choose_profile_image_title"));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) profileImage.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            selectedProfileImagePath = file.toURI().toString();
            profileImage.setImage(new Image(selectedProfileImagePath));
        }
    }

    @FXML
    private void handleSaveChanges() {
        try {
            UserDTO dto = new UserDTO();
            dto.setId(currentUser.getId());
            dto.setEmail(emailField.getText());

            dto.setPhone(phoneField.getText());
            dto.setDob(dobPicker.getValue());

            dto.setAccessLevel(currentUser.getAccessLevel());


            String[] parts = fullNameField.getText().trim().split("\\s+", 2);
            dto.setFirstName(parts.length > 0 ? parts[0] : "");
            dto.setLastName(parts.length > 1 ? parts[1] : "");

            // Profile image
            dto.setProfileImageUrl(
                    selectedProfileImagePath != null
                            ? selectedProfileImagePath
                            : currentUser.getProfileImageUrl()
            );

            // Update backend and use returned updated user
            currentUser = userService.updateCurrentUser(dto);

            //  Update UI
            fillUIWithUser(currentUser);

            new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("userSettings.update_success")).show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, resourceBundle.getString("userSettings.update_failure")).show();
        }
    }

    private void loadDefaultProfileImage() {
        Image img = new Image(
                getClass().getResource("/com/cems/frontend/view/assets/profile.jpeg").toExternalForm()
        );
        profileImage.setImage(img);
    }

    @FXML private Button deleteButton;

    @FXML
    private void handleDeleteAccount() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                resourceBundle.getString("userSettings.delete_confirmation"));
        confirm.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                try {
                    userService.deleteCurrentUser();
                    new Alert(Alert.AlertType.INFORMATION, resourceBundle.getString("userSettings.delete_success")).show();
                    // Redirect to login
                    SceneNavigator.loadPage("Login.fxml");
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, resourceBundle.getString("userSettings.delete_failure")).show();
                }
            }
        });
    }

}
