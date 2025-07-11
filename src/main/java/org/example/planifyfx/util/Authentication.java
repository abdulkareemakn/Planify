package org.example.planifyfx.util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.example.planifyfx.deps.DBUtils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Authentication {

    public static void loginUser(String username, String password,
                                 Consumer<Boolean> onSuccess,
                                 Consumer<String> onError) {

        Callable<Boolean> taskLogic = () -> {
            String sql = "SELECT password FROM credentials WHERE username = ?";

            return DBUtils.executeQuery(sql, stmt -> stmt.setString(1, username), rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return PasswordUtil.verifyPassword(password, storedHash);
                }
                return false;
            });
        };

        DBUtils.runAsync(taskLogic, onSuccess, ex -> {
            String errorMsg = ex.getMessage() != null ? ex.getMessage() : "Login failed";
            onError.accept(errorMsg);
        });
    }

    public static void userExists(String username,
                                  Consumer<Boolean> onSuccess,
                                  Consumer<String> onError) {

        Callable<Boolean> taskLogic = () -> {
            String sql = "SELECT COUNT(*) FROM credentials WHERE username = ?";
            return DBUtils.executeQuery(sql, stmt -> stmt.setString(1, username), rs -> rs.next() && rs.getInt(1) > 0);
        };

        DBUtils.runAsync(taskLogic, onSuccess, ex -> {
            String errorMsg = ex.getMessage() != null ? ex.getMessage() : "Error checking user";
            onError.accept(errorMsg);
        });
    }

    public static void resetPassword(String username, String newPassword,
                                     Consumer<Boolean> onSuccess,
                                     Consumer<String> onError) {

        userExists(username, exists -> {
            if (!exists) {
                onError.accept("User not found");
                return;
            }

            Callable<Boolean> taskLogic = () -> {
                String hashedPassword = PasswordUtil.createHash(newPassword);
                String sql = "UPDATE credentials SET password = ? WHERE username = ?";

                return DBUtils.executeUpdate(sql, stmt -> {
                    stmt.setString(1, hashedPassword);
                    stmt.setString(2, username);
                });
            };

            DBUtils.runAsync(taskLogic, onSuccess, ex -> {
                String errorMsg = ex.getMessage() != null ? ex.getMessage() : "Password reset failed";
                onError.accept(errorMsg);
            });

        }, onError);
    }

}



