package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Controller for the admin page where admins can manage events.
 */
public class AdminPageController {

  @FXML
  private TableView<Event> eventTable;

  @FXML
  private TableColumn<Event, UUID> idColumn;
  @FXML
  private TableColumn<Event, String> titleColumn;
  @FXML
  private TableColumn<Event, String> descriptionColumn;
  @FXML
  private TableColumn<Event, String> locationColumn;
  @FXML
  private TableColumn<Event, Long> capacityColumn;
  @FXML
  private TableColumn<Event, String> dateColumn;
  @FXML
  private TableColumn<Event, Boolean> approvedColumn;
  @FXML
  private TableColumn<Event, Void> attendanceColumn;
  @FXML
  private TableColumn<Event, Void> actionsColumn;

  @FXML
  private TextField searchField;

  private final ApiEventService eventService = new ApiEventService();
  private final ObservableList<Event> masterData = FXCollections.observableArrayList();
  private ResourceBundle rb;
  private LocaleUtil localeService = LocaleUtil.getInstance();

  /**
   * Initializes the controller. Checks if the user is an admin, sets up table
   * columns, search functionality, and loads events.
   */
  @FXML
  public void initialize() {
    rb = localeService.getBundle(Paths.EVENT_MANAGEMENT);
    String role = LocalStorage.get("role");
    if (!"ADMIN".equals(role)) {
      SceneNavigator.loadPage("home-view.fxml");
      return;
    }
    setupColumns();
    setupSearch();
    loadEvents();
  }

  private void setupColumns() {

    idColumn.setCellValueFactory(cell -> cell.getValue().idProperty());
    titleColumn.setCellValueFactory(cell -> cell.getValue().titleProperty());
    descriptionColumn.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
    locationColumn.setCellValueFactory(cell -> cell.getValue().locationProperty());
    capacityColumn.setCellValueFactory(cell -> cell.getValue().capacityProperty().asObject());

    dateColumn.setCellValueFactory(cell -> {
      if (cell.getValue().getDateTime() == null) {
        return new SimpleStringProperty("");
      }

      DateTimeFormatter formatter = localeService.dateTime(FormatStyle.FULL, FormatStyle.SHORT);
      return new SimpleStringProperty(
          formatter.format(cell.getValue().getDateTime().atZone(ZoneId.systemDefault()))
      );
    });

    approvedColumn.setCellValueFactory(cell -> cell.getValue().approvedProperty().asObject());
    idColumn.setPrefWidth(120);
    titleColumn.setPrefWidth(150);
    descriptionColumn.setPrefWidth(200);
    locationColumn.setPrefWidth(150);
    capacityColumn.setPrefWidth(80);
    dateColumn.setPrefWidth(140);
    approvedColumn.setPrefWidth(80);
    attendanceColumn.setCellFactory(col -> new TableCell<>() {

      private final Button attendanceBtn = new Button(
          rb.getString("eventManagement.view_attendance"));

      {
        attendanceBtn.setOnAction(e -> {
          Event event = getTableView().getItems().get(getIndex());
          SceneNavigator.loadAttendancePage(event);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(attendanceBtn);
        }
      }
    });
    actionsColumn.setPrefWidth(150);

    actionsColumn.setCellFactory(col -> new TableCell<>() {
      private final Button approveBtn = new Button(rb.getString("eventManagement.approve_event"));
      private final Button editBtn = new Button(rb.getString("eventManagement.edit_event"));
      private final Button deleteBtn = new Button(rb.getString("eventManagement.delete_event"));

      {
        approveBtn.setOnAction(e -> {
          Event event = getTableView().getItems().get(getIndex());
          handleApprove(event);
        });
        editBtn.setOnAction(e -> {
          Event event = getTableView().getItems().get(getIndex());
          SceneNavigator.loadEditPage(event);
        });

        deleteBtn.setOnAction(e -> {
          Event event = getTableView().getItems().get(getIndex());
          handleDelete(event);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
          return;
        }

        Event event = getTableView().getItems().get(getIndex());
        HBox box = new HBox(10);
        if (!event.isApproved()) {
          box.getChildren().add(approveBtn);
        }
        box.getChildren().addAll(editBtn, deleteBtn);
        setGraphic(box);
      }
    });
  }

  private void setupSearch() {
    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase();

      eventTable.setItems(masterData.filtered(event ->
          event.getTitle().toLowerCase().contains(filter)
              || event.getLocation().toLowerCase().contains(filter)
              || event.getDescription().toLowerCase().contains(filter)
      ));
    });
  }

  private void loadEvents() {
    Task<List<Event>> task = new Task<>() {
      @Override
      protected List<Event> call() throws Exception {
        return eventService.getAllEvents();
      }
    };

    task.setOnSucceeded(e -> {
      masterData.setAll(task.getValue());
      eventTable.setItems(masterData);
    });

    task.setOnFailed(e -> {
      task.getException().printStackTrace();
      AlertHelper.showError(rb.getString("eventManagement.error_title"),
          rb.getString("eventManagement.load_event_error_message"));
    });

    new Thread(task).start();
  }

  private void handleDelete(Event event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setHeaderText(rb.getString("eventManagement.delete_confirmation_title"));
    alert.setContentText(rb.getString(
        "eventManagement.delete_confirmation_content") + " " + event.getTitle());

    alert.showAndWait().ifPresent(result -> {
      if (result == ButtonType.OK) {
        try {
          eventService.deleteEvent(event.getId().toString());
          masterData.remove(event);
          AlertHelper.showInfo(rb.getString("eventManagement.delete_success_title"),
              rb.getString("eventManagement.delete_success_content"));
        } catch (Exception ex) {
          AlertHelper.showError(rb.getString("eventManagement.error_title"), ex.getMessage());
        }
      }
    });
  }

  /**
   * For testing purposes only. Allows setting event data directly without API calls.
   */
  public void setTestData(List<Event> events) {
    masterData.setAll(events);
    eventTable.setItems(masterData);
  }

  private void handleApprove(Event event) {
    try {
      Event updated = eventService.approveEvent(event.getId().toString());
      event.setApproved(true); // update UI model
      eventTable.refresh();
      AlertHelper.showInfo(rb.getString("eventManagement.approve_success_tile"),
          rb.getString("eventManagement.approve_success_content"));
    } catch (Exception ex) {
      AlertHelper.showError(rb.getString("eventManagement.error_title"), ex.getMessage());
    }
  }
}
