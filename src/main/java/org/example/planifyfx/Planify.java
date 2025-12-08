package org.example.planifyfx;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.planifyfx.util.DatabaseHelper;
import org.example.planifyfx.util.SceneManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Main application class for Planify - an Event Management System.
 * This class initializes the database and starts the JavaFX application.
 */
public class Planify extends Application {

    // Set to false to skip login during development
    private static final boolean AUTH_ENABLED = false;

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the SceneManager with the primary stage
        SceneManager.getInstance().setPrimaryStage(stage);

        // Configure the main window
        stage.setTitle("Planify - Event Management System");
        stage.setWidth(1280);
        stage.setHeight(720);

        // Start with login page or dashboard based on auth setting
        if (AUTH_ENABLED) {
            SceneManager.getInstance().switchScene("LoginPage.fxml");
        } else {
            SceneManager.getInstance().switchScene("Dashboard.fxml");
        }
    }

    public static void main(String[] args) {
        // Initialize database before launching the app
        initializeDatabase();
        
        // Launch the JavaFX application
        launch(args);
    }

    /**
     * Initializes the database by creating the necessary tables.
     * This runs the schema.sql file to set up the database structure.
     */
    private static void initializeDatabase() {
        try {
            // Ensure the db directory exists
            Files.createDirectories(Paths.get("db"));
            
            // Read and execute the schema
            String schemaPath = "src/main/resources/org/example/planifyfx/db/schema.sql";
            String schema = Files.readString(Paths.get(schemaPath));
            
            try (Connection conn = DatabaseHelper.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // SQLite doesn't support multiple statements in one execute
                // So we split by semicolon and execute each statement
                String[] statements = schema.split(";");
                for (String sql : statements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                
                System.out.println("Database initialized successfully.");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
