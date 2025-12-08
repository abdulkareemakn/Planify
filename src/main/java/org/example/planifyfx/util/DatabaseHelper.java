package org.example.planifyfx.util;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database operations.
 * Provides methods to get database connections and run async tasks.
 */
public class DatabaseHelper {

    private static final String DB_URL = "jdbc:sqlite:db/database.db";

    /**
     * Gets a connection to the SQLite database.
     * Always use try-with-resources when calling this method to ensure
     * the connection is properly closed.
     * 
     * Example:
     * try (Connection conn = DatabaseHelper.getConnection()) {
     *     // use connection
     * }
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Runs a database operation on a background thread to avoid freezing the UI.
     * When the operation completes, the onSuccess callback is called on the UI thread.
     * If an error occurs, the onError callback is called on the UI thread.
     * 
     * @param operation The database operation to run (should return a result)
     * @param onSuccess Called with the result when operation succeeds
     * @param onError Called with the exception when operation fails
     * 
     * Example:
     * DatabaseHelper.runAsync(
     *     () -> {
     *         // Database work here (runs on background thread)
     *         return someResult;
     *     },
     *     result -> {
     *         // Handle success (runs on UI thread)
     *         label.setText("Done!");
     *     },
     *     error -> {
     *         // Handle error (runs on UI thread)
     *         error.printStackTrace();
     *     }
     * );
     */
    public static void runAsync(DatabaseOperation operation, 
                                SuccessCallback onSuccess, 
                                ErrorCallback onError) {
        
        Task<Object> task = new Task<>() {
            @Override
            protected Object call() throws Exception {
                return operation.execute();
            }
        };

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> onSuccess.onSuccess(task.getValue()));
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> onError.onError(task.getException()));
        });

        // Start the task on a new thread
        Thread thread = new Thread(task);
        thread.setDaemon(true);  // Allow app to exit even if thread is running
        thread.start();
    }

    /**
     * Runs a database operation that doesn't return a result (like INSERT, UPDATE, DELETE).
     * 
     * @param operation The database operation to run
     * @param onSuccess Called when operation succeeds
     * @param onError Called with the exception when operation fails
     */
    public static void runAsyncUpdate(UpdateOperation operation,
                                      Runnable onSuccess,
                                      ErrorCallback onError) {
        
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                operation.execute();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            Platform.runLater(onSuccess);
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> onError.onError(task.getException()));
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Interface for database operations that return a result.
     */
    public interface DatabaseOperation {
        Object execute() throws Exception;
    }

    /**
     * Interface for database operations that don't return a result.
     */
    public interface UpdateOperation {
        void execute() throws Exception;
    }

    /**
     * Callback interface for successful operations.
     */
    public interface SuccessCallback {
        void onSuccess(Object result);
    }

    /**
     * Callback interface for failed operations.
     */
    public interface ErrorCallback {
        void onError(Throwable error);
    }
}
