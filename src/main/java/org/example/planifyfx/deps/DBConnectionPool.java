package org.example.planifyfx.deps;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class DBConnectionPool {
    private static HikariDataSource dataSource;

    public static void initializeDB(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setConnectionTestQuery("PRAGMA foreign_keys = ON");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection() {
        dataSource.close();
    }
}
