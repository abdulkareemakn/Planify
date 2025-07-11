package org.example.planifyfx.util;


import org.example.planifyfx.deps.DBConnectionPool;
import org.example.planifyfx.deps.DBUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:sqlite:db/database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    static {
        initializeDatabase();
        DBConnectionPool.initializeDB(DB_URL);
    }

    private static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String schema = Files.readString(Paths.get("src/main/resources/org/example/planifyfx/db/schema.sql"));
            stmt.executeUpdate(schema);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

