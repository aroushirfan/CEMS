package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Paths;
import com.cems.frontend.models.User;
import com.cems.frontend.services.UserService;
import com.cems.frontend.utils.LocaleUtil;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for the User Management page. Allows admins to view all
 * users and manage their roles.
 */
public class UserManagementController {

  @FXML
  private TableView<User> userTable;
  @FXML
  private TableColumn<User, String> idColumn;
  @FXML
  private TableColumn<User, String> emailColumn;
  @FXML
  private TableColumn<User, String> firstNameColumn;
  @FXML
  private TableColumn<User, String> lastNameColumn;
  @FXML
  private TableColumn<User, Number> accessLevelColumn;
  @FXML
  private TableColumn<User, Void> actionsColumn;

  @FXML
  private TextField searchField;

  private final UserService userService = new UserService();
  private final ObservableList<User> masterData = FXCollections.observableArrayList();
  private ResourceBundle rb;
  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the controller by setting up table columns, search functionality,
   * and loading user data.
   */
  @FXML
  public void initialize() {
    this.rb = LocaleUtil.getInstance().getBundle(Paths.USER_MANAGEMENT);
    setupColumns();
    setupSearch();
    loadUsers();
  }

  private void setupColumns() {
    idColumn.setCellValueFactory(cell -> cell.getValue().idProperty().asString());
    emailColumn.setCellValueFactory(cell -> cell.getValue().emailProperty());
    firstNameColumn.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastNameColumn.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    accessLevelColumn.setCellValueFactory(cell -> cell.getValue().accessLevelProperty());

    actionsColumn.setCellFactory(col -> new TableCell<>() {

      private final Button makeFaculty = new Button(rb.getString("user_management.make_faculty"));
      private final Button makeAdmin = new Button(rb.getString("user_management.make_admin"));
      private final Button removeFaculty = new Button(
          rb.getString("user_management.remove_faculty"));
      private final Button removeAdmin = new Button(rb.getString("user_management.remove_admin"));

      {
        makeFaculty.setOnAction(e -> updateRole(1));
        makeAdmin.setOnAction(e -> updateRole(2));
        removeFaculty.setOnAction(e -> updateRole(0));
        removeAdmin.setOnAction(e -> updateRole(0));
      }

      private void updateRole(int newRole) {
        User user = getTableView().getItems().get(getIndex());
        try {
          userService.updateAccessLevel(user.getId().toString(), newRole);
          loadUsers();
        } catch (Exception ex) {
          if (ex instanceof InterruptedException) {
            Thread.currentThread().interrupt();
          }
          logger.log(Level.WARNING, ex.getMessage(), ex);
        }
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
          return;
        }

        User user = getTableView().getItems().get(getIndex());
        HBox box = new HBox(10);

        switch (user.getAccessLevel()) {
          case 0 -> box.getChildren().addAll(makeFaculty, makeAdmin);
          case 1 -> box.getChildren().addAll(removeFaculty, makeAdmin);
          case 2 -> box.getChildren().addAll(removeAdmin);
          default -> {
            // leave empty due to no role assigned
          }
        }

        setGraphic(box);
      }
    });
  }

  private void setupSearch() {
    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase();
      userTable.setItems(masterData.filtered(user ->
          user.getEmail().toLowerCase().contains(filter)
              || user.getFirstName().toLowerCase().contains(filter)
              || user.getLastName().toLowerCase().contains(filter)
      ));
    });
  }

  private void loadUsers() {
    try {
      masterData.setAll(userService.getAllUsers());
      userTable.setItems(masterData);
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      logger.log(Level.WARNING, e.getMessage(), e);
    }
  }
}