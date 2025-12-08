package org.example.planifyfx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.util.DatabaseHelper;
import org.example.planifyfx.util.SceneManager;

/**
 * Controller for the Events list screen.
 * Displays all events in a table and allows viewing details and deletion.
 */
public class EventsController implements Initializable {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button createEventBtn;
    @FXML
    private TableView<EventTableRow> eventsTable;
    @FXML
    private TableColumn<EventTableRow, Integer> idColumn;
    @FXML
    private TableColumn<EventTableRow, String> nameColumn;
    @FXML
    private TableColumn<EventTableRow, String> typeColumn;
    @FXML
    private TableColumn<EventTableRow, String> dateColumn;
    @FXML
    private TableColumn<EventTableRow, String> timeColumn;
    @FXML
    private TableColumn<EventTableRow, String> clientColumn;
    @FXML
    private TableColumn<EventTableRow, Integer> attendanceColumn;
    @FXML
    private TableColumn<EventTableRow, String> specialDetailsColumn;
    @FXML
    private TableColumn<EventTableRow, Void> actionsColumn;

    private ObservableList<EventTableRow> eventData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        eventsTable.setItems(eventData);
        loadEvents();
    }

    /**
     * Sets up the table columns with their cell value factories and custom cell factories.
     */
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        specialDetailsColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getSpecialDetails()));

        // Custom cell factory for special details with color coding by event type
        specialDetailsColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    EventTableRow row = getTableView().getItems().get(getIndex());
                    switch (row.getEventType()) {
                        case "Wedding" -> setStyle("-fx-text-fill: #2980b9;");
                        case "Birthday" -> setStyle("-fx-text-fill: #27ae60;");
                        case "Seminar" -> setStyle("-fx-text-fill: #e67e22;");
                        default -> setStyle("");
                    }
                }
            }
        });

        // Custom cell factory for action buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttonBox = new HBox(5);

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 4 8;");
                deleteBtn.setOnAction(e -> deleteEvent(getTableView().getItems().get(getIndex())));
                buttonBox.setAlignment(Pos.CENTER);
                buttonBox.getChildren().addAll(deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });

        // Double-click to show event details
        eventsTable.setRowFactory(tv -> {
            TableRow<EventTableRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showEventDetails(row.getItem());
                }
            });
            return row;
        });
    }

    /**
     * Loads events from the database asynchronously.
     */
    private void loadEvents() {
        eventData.clear();

        DatabaseHelper.runAsync(
            () -> EventRepository.fetchAllEvents(),
            result -> {
                @SuppressWarnings("unchecked")
                List<EventTableRow> events = (List<EventTableRow>) result;
                eventData.setAll(events);
                eventsTable.refresh();
            },
            error -> {
                error.printStackTrace();
                showErrorAlert("Database Error", "Failed to load events: " + error.getMessage());
            }
        );
    }

    /**
     * Shows detailed information about an event in a popup dialog.
     */
    private void showEventDetails(EventTableRow eventRow) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Details");
        alert.setHeaderText(eventRow.getName() + " (" + eventRow.getEventType() + ")");

        StringBuilder details = new StringBuilder();
        details.append("ID: ").append(eventRow.getId()).append("\n")
                .append("Name: ").append(eventRow.getName()).append("\n")
                .append("Type: ").append(eventRow.getEventType()).append("\n")
                .append("Date: ").append(eventRow.getDate()).append("\n")
                .append("Time: ").append(eventRow.getTime()).append("\n")
                .append("Client: ").append(eventRow.getClientName()).append("\n")
                .append("Venue: ").append(eventRow.getVenueName() != null ? eventRow.getVenueName() : "Not specified").append("\n")
                .append("Attendance: ").append(eventRow.getAttendance()).append("\n");

        switch (eventRow.getEventType()) {
            case "Wedding" -> details
                .append("Bride: ").append(eventRow.getBrideName()).append("\n")
                .append("Groom: ").append(eventRow.getGroomName()).append("\n")
                .append("Photographer Required: ").append(eventRow.getPhotographerRequired() ? "Yes" : "No").append("\n");
            case "Birthday" -> details
                .append("Age: ").append(eventRow.getAge()).append("\n")
                .append("Theme: ").append(eventRow.getTheme()).append("\n")
                .append("Number of Kids: ").append(eventRow.getNumberOfKids()).append("\n");
            case "Seminar" -> details
                .append("Chief Guest: ").append(eventRow.getChiefGuest()).append("\n")
                .append("Speaker: ").append(eventRow.getSpeaker()).append("\n")
                .append("Topic: ").append(eventRow.getTopic()).append("\n");
        }

        alert.setContentText(details.toString());
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }

    @FXML
    private void showDashboard() {
        SceneManager.getInstance().switchScene("Dashboard.fxml");
    }

    @FXML
    private void showEvents() {
        refreshEvents();
    }

    @FXML
    private void createNewEvent() {
        SceneManager.getInstance().switchScene("CreateEvent.fxml");
    }

    /**
     * Deletes an event after user confirmation.
     */
    private void deleteEvent(EventTableRow eventRow) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Delete Event: " + eventRow.getName());
        alert.setContentText("Are you sure you want to delete this event? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                EventRepository.deleteById(
                    eventRow.getId(),
                    () -> {
                        // Success - remove from table
                        eventData.remove(eventRow);
                        eventsTable.refresh();
                        System.out.println("Deleted event: " + eventRow.getName());
                    },
                    error -> {
                        error.printStackTrace();
                        showErrorAlert("Delete Error", "Failed to delete event: " + error.getMessage());
                    }
                );
            }
        });
    }

    /**
     * Shows an error alert dialog.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Refreshes the events list.
     */
    public void refreshEvents() {
        loadEvents();
    }

    /**
     * Inner class representing a row in the events table.
     * Contains all event data including type-specific fields.
     */
    public static class EventTableRow {
        private final int id;
        private final String name;
        private final String eventType;
        private final String date;
        private final String time;
        private final String clientName;
        private final int attendance;
        private final String venueName;
        
        // Wedding-specific fields
        private final String brideName;
        private final String groomName;
        private final Boolean photographerRequired;
        
        // Birthday-specific fields
        private final Integer age;
        private final String theme;
        private final Integer numberOfKids;
        
        // Seminar-specific fields
        private final String chiefGuest;
        private final String speaker;
        private final String topic;

        public EventTableRow(int id, String name, String eventType, String date, String time, 
                             String clientName, int attendance, String venueName,
                             String brideName, String groomName, Boolean photographerRequired,
                             Integer age, String theme, Integer numberOfKids, 
                             String chiefGuest, String speaker, String topic) {
            this.id = id;
            this.name = name;
            this.eventType = eventType;
            this.date = date;
            this.time = time;
            this.clientName = clientName;
            this.attendance = attendance;
            this.venueName = venueName;
            this.brideName = brideName;
            this.groomName = groomName;
            this.photographerRequired = photographerRequired;
            this.age = age;
            this.theme = theme;
            this.numberOfKids = numberOfKids;
            this.chiefGuest = chiefGuest;
            this.speaker = speaker;
            this.topic = topic;
        }

        // Getters
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEventType() { return eventType; }
        public String getDate() { return date; }
        public String getTime() { return time; }
        public String getClientName() { return clientName; }
        public int getAttendance() { return attendance; }
        public String getVenueName() { return venueName; }
        public String getBrideName() { return brideName; }
        public String getGroomName() { return groomName; }
        public Boolean getPhotographerRequired() { return photographerRequired; }
        public Integer getAge() { return age; }
        public String getTheme() { return theme; }
        public Integer getNumberOfKids() { return numberOfKids; }
        public String getChiefGuest() { return chiefGuest; }
        public String getSpeaker() { return speaker; }
        public String getTopic() { return topic; }

        /**
         * Returns a formatted string of type-specific event details.
         */
        public String getSpecialDetails() {
            return switch (eventType) {
                case "Wedding" -> String.format("Bride: %s, Groom: %s, Photographer: %s",
                        brideName, groomName, photographerRequired ? "Yes" : "No");
                case "Birthday" -> String.format("Age: %s, Theme: %s, Kids: %s",
                        age, theme, numberOfKids);
                case "Seminar" -> String.format("Chief Guest: %s, Speaker: %s, Topic: %s",
                        chiefGuest, speaker, topic);
                default -> "N/A";
            };
        }
    }
}
