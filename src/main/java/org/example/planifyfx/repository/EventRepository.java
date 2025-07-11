package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.concurrent.Task;
import org.example.planifyfx.controller.EventsController;
import org.example.planifyfx.deps.DBUtils;
import org.example.planifyfx.model.*;
import org.example.planifyfx.util.ContactInfo;
import org.example.planifyfx.util.DatabaseUtil;
import org.example.planifyfx.util.EventInfo;
import org.example.planifyfx.util.ConcreteEvent;

public class EventRepository {

    public static void save(Event event) {

        String sql = "INSERT INTO events (name, time, attendance, event_type, client_id) VALUES (?, ?, ?, ?, ?)";

        DBUtils.executeInsertWithGeneratedKeyAsync(sql, stmt -> {
            stmt.setString(1, event.getName());
            stmt.setString(2, event.getTime());
            stmt.setInt(3, event.getAttendance());
            stmt.setString(4, event.getEventType());
            stmt.setInt(5, event.getClient().getId());
        }, generatedId -> {
            event.setId(generatedId);

            if (event instanceof WeddingEvent weddingEvent) {
                String wedSql = "INSERT INTO wedding_event (event_id, bride_name, groom_name, photographer_required) VALUES (?, ?, ?, ?)";
                DBUtils.executeUpdateAsync(wedSql, wstmt -> {
                    wstmt.setInt(1, generatedId);
                    wstmt.setString(2, weddingEvent.getBrideName());
                    wstmt.setString(3, weddingEvent.getGroomName());
                    wstmt.setInt(4, weddingEvent.isPhotographerRequired() ? 1 : 0);
                }, success -> System.out.println("Wedding event saved."), Throwable::printStackTrace);
            } else if (event instanceof BirthdayEvent birthdayEvent) {
                String bdaySql = "INSERT INTO birthday_event (event_id, age, theme, number_of_kids) VALUES (?, ?, ?, ?)";
                DBUtils.executeUpdateAsync(bdaySql, bstmt -> {
                    bstmt.setInt(1, generatedId);
                    bstmt.setInt(2, birthdayEvent.getAge());
                    bstmt.setString(3, birthdayEvent.getTheme());
                    bstmt.setInt(4, birthdayEvent.getNumberOfKids());
                }, success -> System.out.println("Birthday event saved."), Throwable::printStackTrace);
            } else if (event instanceof SeminarEvent seminarEvent) {
                String seminarSql = "INSERT INTO seminar_event (event_id, chief_guest, speaker, topic) VALUES (?, ?, ?, ?)";
                DBUtils.executeUpdateAsync(seminarSql, preparedStatement -> {
                    preparedStatement.setInt(1, generatedId);
                    preparedStatement.setString(2, seminarEvent.getChiefGuest());
                    preparedStatement.setString(3, seminarEvent.getSpeaker());
                    preparedStatement.setString(4, seminarEvent.getTopic());
                }, success -> System.out.println("Seminar Event saved"), Throwable::printStackTrace);
            }
        }, Throwable::printStackTrace);

    }

    public static List<EventsController.EventTableRow> fetchAllEvents() throws SQLException {
        List<EventsController.EventTableRow> events = new ArrayList<>();

        String sql = """
                SELECT
                    e.event_id,
                    e.name AS event_name,
                    e.event_type,
                    e.time AS event_time,
                    c.name AS client_name,
                    e.attendance,
                    w.bride_name,
                    w.groom_name,
                    w.photographer_required,
                    b.age,
                    b.theme,
                    b.number_of_kids,
                    s.chief_guest,
                    s.speaker,
                    s.topic
                FROM events e
                JOIN clients c ON e.client_id = c.client_id
                LEFT JOIN wedding_event w ON e.event_id = w.event_id
                LEFT JOIN birthday_event b ON e.event_id = b.event_id
                LEFT JOIN seminar_event s ON e.event_id = s.event_id
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("event_id");
                String name = rs.getString("event_name");
                String type = rs.getString("event_type");
                String eventTime = rs.getString("event_time");
                String[] dateTime = eventTime.split(" ");
                String date = dateTime.length > 0 ? dateTime[0] : "";
                String time = dateTime.length > 1 ? dateTime[1] : "";
                String clientName = rs.getString("client_name");
                int attendance = rs.getInt("attendance");

                String brideName = rs.getString("bride_name");
                String groomName = rs.getString("groom_name");
                Integer photographerRequiredInt = rs.getInt("photographer_required");
                Boolean photographerRequired = rs.wasNull() ? null : (photographerRequiredInt == 1);

                Integer age = rs.getInt("age");
                if (rs.wasNull()) age = null;
                String theme = rs.getString("theme");
                Integer numberOfKids = rs.getInt("number_of_kids");
                if (rs.wasNull()) numberOfKids = null;

                String chiefGuest = rs.getString("chief_guest");
                String speaker = rs.getString("speaker");
                String topic = rs.getString("topic");

                events.add(new EventsController.EventTableRow(
                        id, name, type, date, time, clientName, attendance,
                        brideName, groomName, photographerRequired, age, theme, numberOfKids, chiefGuest, speaker, topic
                ));
            }
        }

        return events;
    }

    public static int getTotalEventsCount() {
        String sql = "SELECT total_events FROM statistics";
        int count = 0;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                count = rs.getInt("total_events");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getWeddingEventsCount() {
        String sql = "SELECT total_wedding_events FROM statistics";
        int count = 0;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            count = rs.next() ? rs.getInt("total_wedding_events") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    public static int getBirthdayEventsCount() {
        String sql = "SELECT total_birthday_events FROM statistics";
        int count = 0;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            count = rs.next() ? rs.getInt("total_birthday_events") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getSeminarEventsCount() {
        String sql = "SELECT total_seminar_events FROM statistics";
        int count = 0;

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            count = rs.next() ? rs.getInt("total_seminar_events") : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


}