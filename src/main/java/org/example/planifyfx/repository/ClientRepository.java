package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import org.example.planifyfx.deps.DBUtils;
import org.example.planifyfx.model.Client;
import org.example.planifyfx.util.ContactInfo;
import org.example.planifyfx.util.DatabaseUtil;

public class ClientRepository {
    public static void save(Client client, Consumer<Integer> onSuccess, Consumer<Throwable> onError) {

        String sql = "INSERT INTO clients (name, email_address, phone_number) VALUES (?, ?, ?)";

        DBUtils.executeInsertWithGeneratedKeyAsync(sql, statement -> {
                    statement.setString(1, client.getName());
                    statement.setString(2, client.getEmailAddress());
                    statement.setString(3, client.getPhoneNumber());
                }, generatedId -> {
                    client.setId(generatedId);
                    onSuccess.accept(generatedId);
                },
                error -> error.printStackTrace());


    }
}