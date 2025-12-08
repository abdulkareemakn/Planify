package org.example.planifyfx.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

/**
 * Handles user authentication operations like login and password reset.
 * All database operations are performed asynchronously to avoid freezing the UI.
 */
public class Authentication {

    /**
     * Attempts to log in a user with the given credentials.
     * 
     * @param username The username to check
     * @param password The password to verify
     * @param onSuccess Called with true if login succeeds, false otherwise
     * @param onError Called with error message if something goes wrong
     */
    public static void loginUser(String username, String password,
                                 Consumer<Boolean> onSuccess,
                                 Consumer<String> onError) {

        DatabaseHelper.runAsync(
            () -> {
                String sql = "SELECT password FROM credentials WHERE username = ?";
                
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, username);
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String storedHash = rs.getString("password");
                            return PasswordUtil.verifyPassword(password, storedHash);
                        }
                        return false;
                    }
                }
            },
            result -> onSuccess.accept((Boolean) result),
            error -> {
                String errorMsg = error.getMessage() != null ? error.getMessage() : "Login failed";
                onError.accept(errorMsg);
            }
        );
    }

    /**
     * Checks if a user exists in the database.
     * 
     * @param username The username to check
     * @param onSuccess Called with true if user exists, false otherwise
     * @param onError Called with error message if something goes wrong
     */
    public static void userExists(String username,
                                  Consumer<Boolean> onSuccess,
                                  Consumer<String> onError) {

        DatabaseHelper.runAsync(
            () -> {
                String sql = "SELECT COUNT(*) FROM credentials WHERE username = ?";
                
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, username);
                    
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() && rs.getInt(1) > 0;
                    }
                }
            },
            result -> onSuccess.accept((Boolean) result),
            error -> {
                String errorMsg = error.getMessage() != null ? error.getMessage() : "Error checking user";
                onError.accept(errorMsg);
            }
        );
    }

    /**
     * Resets the password for a user.
     * First checks if the user exists, then updates the password.
     * 
     * @param username The username whose password to reset
     * @param newPassword The new password (will be hashed before storing)
     * @param onSuccess Called with true if reset succeeds
     * @param onError Called with error message if something goes wrong
     */
    public static void resetPassword(String username, String newPassword,
                                     Consumer<Boolean> onSuccess,
                                     Consumer<String> onError) {

        // First check if user exists
        userExists(username, exists -> {
            if (!exists) {
                onError.accept("User not found");
                return;
            }

            // User exists, now update password
            DatabaseHelper.runAsync(
                () -> {
                    String hashedPassword = PasswordUtil.createHash(newPassword);
                    String sql = "UPDATE credentials SET password = ? WHERE username = ?";
                    
                    try (Connection conn = DatabaseHelper.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        
                        stmt.setString(1, hashedPassword);
                        stmt.setString(2, username);
                        
                        int rowsAffected = stmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                },
                result -> onSuccess.accept((Boolean) result),
                error -> {
                    String errorMsg = error.getMessage() != null ? error.getMessage() : "Password reset failed";
                    onError.accept(errorMsg);
                }
            );

        }, onError);
    }
}
