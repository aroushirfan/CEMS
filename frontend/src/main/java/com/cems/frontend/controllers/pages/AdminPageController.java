package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.view.AlertHelper;
import com.cems.frontend.view.SceneNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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

    @FXML
    public void initialize() {
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
            if (cell.getValue().getDateTime() == null)
                return new SimpleStringProperty("");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

            private final Button attendanceBtn = new Button("View");

            {
                attendanceBtn.setOnAction(e -> {
                    Event event = getTableView().getItems().get(getIndex());
                    SceneNavigator.loadAttendancePage(event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(attendanceBtn);
            }
        });
        actionsColumn.setPrefWidth(150);

        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

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
                    event.getTitle().toLowerCase().contains(filter) ||
                            event.getLocation().toLowerCase().contains(filter) ||
                            event.getDescription().toLowerCase().contains(filter)
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
            AlertHelper.showError("Error", "Failed to load events.");
        });

        new Thread(task).start();
    }

    private void handleDelete(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete event?");
        alert.setContentText("Are you sure you want to delete: " + event.getTitle());

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    eventService.deleteEvent(event.getId().toString());
                    masterData.remove(event);
                    AlertHelper.showInfo("Deleted", "Event removed successfully.");
                } catch (Exception ex) {
                    AlertHelper.showError("Error", ex.getMessage());
                }
            }
        });
    }

    public void setTestData(List<Event> events) {
        masterData.setAll(events);
        eventTable.setItems(masterData);
    }

    private void handleApprove(Event event) {
        try {
            Event updated = eventService.approveEvent(event.getId().toString());
            event.setApproved(true); // update UI model
            eventTable.refresh();
            AlertHelper.showInfo("Approved", "Event approved successfully.");
        } catch (Exception ex) {
            AlertHelper.showError("Error", ex.getMessage());
        }
    }
}
