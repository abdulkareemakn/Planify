package org.example.planifyfx;

import org.example.planifyfx.deps.DBConnectionPool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.planifyfx.controller.DashboardController;
import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.util.DatabaseUtil;
import org.example.planifyfx.util.SceneManager;
import org.example.planifyfx.util.Statistics;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Planify extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        SceneManager.getInstance().setPrimaryStage(stage);

        stage.setTitle("Planify");
        stage.setWidth(1920);
        stage.setHeight(1080);

        SceneManager.getInstance().switchScene("LoginPage.fxml");
    }


    public static void main(String[] args) {

        new DatabaseUtil();
        launch();


        DashboardController dashboardController = new DashboardController();

        try (java.sql.Connection conn = DatabaseUtil.getConnection();
        ) {

            String sql = """
                    DROP TABLE statistics;
                    CREATE TABLE IF NOT EXISTS statistics ( 
                    total_events INTEGER, 
                    total_wedding_events INTEGER, 
                    total_birthday_events INTEGER,
                    total_seminar_events INTEGER 
                    );
                    """;

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            String insertData = "INSERT INTO statistics (total_events, total_wedding_events, total_birthday_events, total_seminar_events) VALUES (?, ?, ?, ?);";

            PreparedStatement pstmt = conn.prepareStatement(insertData);
            pstmt.setInt(1, Statistics.totalEvents);
            pstmt.setInt(2, Statistics.totalWeddingEvents);
            pstmt.setInt(3, Statistics.totalBirthdayEvents);
            pstmt.setInt(4, Statistics.totalSeminarEvents);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
