package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;

import org.example.planifyfx.model.Client;
import org.example.planifyfx.util.DatabaseHelper;

/**
 * Repository class for Client database operations.
 * Handles saving and retrieving client information.
 */
public class ClientRepository {
    
    /**
     * Saves a client to the database asynchronously.
     * After saving, the client's ID is set to the generated database ID.
     * 
     * @param client The client to save
     * @param onSuccess Called with the generated client ID when save succeeds
     * @param onError Called with the exception if save fails
     */
    public static void save(Client client, Consumer<Integer> onSuccess, Consumer<Throwable> onError) {
        String sql = "INSERT INTO clients (name, email_address, phone_number) VALUES (?, ?, ?)";

        DatabaseHelper.runAsync(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    
                    stmt.setString(1, client.getName());
                    stmt.setString(2, client.getEmail());
                    stmt.setString(3, client.getPhoneNumber());
                    
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new RuntimeException("Creating client failed, no rows affected.");
                    }
                    
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            client.setId(generatedId);
                            return generatedId;
                        } else {
                            throw new RuntimeException("Creating client failed, no ID obtained.");
                        }
                    }
                }
            },
            result -> onSuccess.accept((Integer) result),
            error -> onError.accept(error)
        );
    }

    /**
     * Updates an existing client in the database asynchronously.
     * 
     * @param client The client with updated values (must have ID set)
     * @param onSuccess Called when the update succeeds
     * @param onError Called with the exception if update fails
     */
    public static void update(Client client, Runnable onSuccess, Consumer<Throwable> onError) {
        String sql = "UPDATE clients SET name = ?, email_address = ?, phone_number = ? WHERE client_id = ?";

        DatabaseHelper.runAsyncUpdate(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, client.getName());
                    stmt.setString(2, client.getEmail());
                    stmt.setString(3, client.getPhoneNumber());
                    stmt.setInt(4, client.getId());
                    
                    stmt.executeUpdate();
                }
            },
            onSuccess,
            error -> onError.accept(error)
        );
    }
}
