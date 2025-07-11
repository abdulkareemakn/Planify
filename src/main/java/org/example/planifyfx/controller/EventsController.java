package org.example.planifyfx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.util.DatabaseUtil;
import org.example.planifyfx.util.Statistics;
import org.example.planifyfx.util.SceneManager;
import org.example.planifyfx.deps.DBUtils;

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

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));
        specialDetailsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecialDetails()));


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
                    if ("Wedding".equals(row.getEventType())) {
                        setStyle("-fx-text-fill: #2980b9;");
                    } else if ("Birthday".equals(row.getEventType())) {
                        setStyle("-fx-text-fill: #27ae60;");
                    } else if ("Seminar".equals(row.getEventType())) {
                        setStyle("-fx-text-fill: #e67e22;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

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

    private void loadEvents() {
        eventData.clear();

        DBUtils.runAsync(
                EventRepository::fetchAllEvents,
                events -> {
                    eventData.setAll(events);
                    eventsTable.refresh();
                },
                ex -> {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Database Error");
                    alert.setContentText("Failed to load events: " + ex.getMessage());
                    alert.showAndWait();
                }
        );

    }

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
                .append("Attendance: ").append(eventRow.getAttendance()).append("\n");

        if ("Wedding".equals(eventRow.getEventType())) {
            details.append("Bride: ").append(eventRow.getBrideName()).append("\n")
                    .append("Groom: ").append(eventRow.getGroomName()).append("\n")
                    .append("Photographer Required: ").append(eventRow.getPhotographerRequired()).append("\n");
        } else if ("Birthday".equals(eventRow.getEventType())) {
            details.append("Age: ").append(eventRow.getAge()).append("\n")
                    .append("Theme: ").append(eventRow.getTheme()).append("\n")
                    .append("Number of Kids: ").append(eventRow.getNumberOfKids()).append("\n");
        } else if ("Seminar".equals(eventRow.getEventType())) {
            details.append("Chief Guest: ").append(eventRow.getChiefGuest()).append("\n")
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
        System.out.println("Navigate to Dashboard");
    }

    @FXML
    private void showEvents() {
        refreshEvents();
    }

    @FXML
    private void createNewEvent() {
        SceneManager.getInstance().switchScene("CreateEvent.fxml");
        System.out.println("Navigate to Create Event page");
    }

    private void deleteEvent(EventTableRow eventRow) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Event");
        alert.setHeaderText("Delete Event: " + eventRow.getName());
        alert.setContentText("Are you sure you want to delete this event? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String sql = "DELETE FROM events WHERE event_id = ?";

                DBUtils.executeUpdateAsync(sql, stmt -> {
                    stmt.setInt(1, eventRow.getId());
                }, success -> {
                    if (success) {
                        eventData.remove(eventRow);
                        eventsTable.refresh();
                        System.out.println("Deleted event: " + eventRow.getName());
                    } else {
                        System.out.println("Event not found or couldn't be deleted.");
                    }
                }, Throwable::printStackTrace);

                String type = eventRow.getEventType();
                if (type.equals("Wedding")) Statistics.totalWeddingEvents--;
                else if (type.equals("Birthday")) Statistics.totalBirthdayEvents--;
                else if (type.equals("Seminar")) Statistics.totalSeminarEvents--;
                Statistics.totalEvents--;
            }
        });
    }

    public void refreshEvents() {
        loadEvents();
    }

    public static class EventTableRow {
        private final int id;
        private final String name;
        private final String eventType;
        private final String date;
        private final String time;
        private final String clientName;
        private final int attendance;
        private final String brideName;
        private final String groomName;
        private final Boolean photographerRequired;
        private final Integer age;
        private final String theme;
        private final Integer numberOfKids;
        private final String chiefGuest;
        private final String speaker;
        private final String topic;

        public EventTableRow(int id, String name, String eventType, String date, String time, String clientName, int attendance,
                             String brideName, String groomName, Boolean photographerRequired,
                             Integer age, String theme, Integer numberOfKids, String chiefGuest, String speaker, String topic) {
            this.id = id;
            this.name = name;
            this.eventType = eventType;
            this.date = date;
            this.time = time;
            this.clientName = clientName;
            this.attendance = attendance;
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

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEventType() {
            return eventType;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getClientName() {
            return clientName;
        }

        public int getAttendance() {
            return attendance;
        }

        public String getBrideName() {
            return brideName;
        }

        public String getGroomName() {
            return groomName;
        }

        public Boolean getPhotographerRequired() {
            return photographerRequired;
        }

        public Integer getAge() {
            return age;
        }

        public String getTheme() {
            return theme;
        }

        public Integer getNumberOfKids() {
            return numberOfKids;
        }

        public String getChiefGuest() {
            return chiefGuest;
        }

        public String getSpeaker() {
            return speaker;
        }

        public String getTopic() {
            return topic;
        }

        public String getSpecialDetails() {
            if ("Wedding".equals(eventType)) {
                return String.format("Bride: %s, Groom: %s, Photographer Required: %s",
                        brideName,
                        groomName,
                        photographerRequired ? "Yes" : "No");
            } else if ("Birthday".equals(eventType)) {
                return String.format("Age: %s, Theme: %s, Kids: %s",
                        age,
                        theme,
                        numberOfKids);
            } else if ("Seminar".equals(eventType)) {
                return String.format("Chief Guest: %s, Speaker: %s, Topic: %s",
                        chiefGuest,
                        speaker,
                        topic);
            }
            return "N/A";
        }
    }
}

