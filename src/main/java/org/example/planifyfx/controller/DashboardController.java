package org.example.planifyfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import org.example.planifyfx.util.SceneManager;
import org.example.planifyfx.util.Statistics;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button addEventBtn;

    @FXML
    private Label totalEventsLabel;
    @FXML
    private Label weddingEventsLabel;
    @FXML
    private Label birthdayEventsLabel;
    @FXML
    private Label seminarEventsLabel;

    @FXML
    private VBox recentEventsContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadDashboardData();

        setActiveNavButton(dashboardBtn);
    }

    private void loadDashboardData() {
        loadGeneralStatistics();
        loadEventTypeStatistics();
    }

    private void loadGeneralStatistics() {
        totalEventsLabel.setText(String.valueOf(Statistics.totalEvents));
    }

    private void loadEventTypeStatistics() {
        weddingEventsLabel.setText(String.valueOf(Statistics.totalWeddingEvents));
        birthdayEventsLabel.setText(String.valueOf(Statistics.totalBirthdayEvents));
        seminarEventsLabel.setText(String.valueOf(Statistics.totalSeminarEvents));
    }

    private HBox createEventRow(String eventInfo) {
        HBox row = new HBox(10);
        row.setStyle("-fx-padding: 8; -fx-background-color: #f8f9fa; -fx-background-radius: 4;");

        Label eventLabel = new Label(eventInfo);
        Label dateLabel = new Label("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        dateLabel.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

        row.getChildren().addAll(eventLabel, dateLabel);
        return row;
    }

    @FXML
    private void showDashboard() {
        setActiveNavButton(dashboardBtn);
    }

    @FXML
    private void showEvents() {
        setActiveNavButton(eventsBtn);

        try {
            SceneManager.getInstance().switchScene("Events.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setActiveNavButton(dashboardBtn);
        }
    }

    @FXML
    private void addNewEvent() {
        setActiveNavButton(addEventBtn);

        try {
            SceneManager.getInstance().switchScene("CreateEvent.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setActiveNavButton(dashboardBtn);
        }
    }

    private void setActiveNavButton(Button activeButton) {
        dashboardBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 4;");
        eventsBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 4;");

        activeButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 4;");
    }

    public void refreshDashboard() {
        loadDashboardData();
    }
}