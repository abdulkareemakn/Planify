package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.example.planifyfx.controller.EventsController;
import org.example.planifyfx.model.*;
import org.example.planifyfx.util.DatabaseHelper;

/**
 * Repository class for Event database operations.
 * Handles saving, retrieving, and deleting events.
 */
public class EventRepository {

    /**
     * Saves an event to the database asynchronously.
     * This method saves the base event first, then saves the type-specific details.
     * 
     * @param event The event to save (can be WeddingEvent, BirthdayEvent, or SeminarEvent)
     * @param onSuccess Called when the event is saved successfully
     * @param onError Called with the exception if save fails
     */
    public static void save(Event event, Runnable onSuccess, Consumer<Throwable> onError) {
        String sql = "INSERT INTO events (name, time, attendance, event_type, client_id, venue_id) VALUES (?, ?, ?, ?, ?, ?)";

        DatabaseHelper.runAsync(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    
                    stmt.setString(1, event.getName());
                    stmt.setString(2, event.getFormattedDateTime());
                    stmt.setInt(3, event.getAttendance());
                    stmt.setString(4, event.getEventType());
                    stmt.setInt(5, event.getClient().getId());
                    
                    // Venue is optional - can be null
                    if (event.getVenue() != null) {
                        stmt.setInt(6, event.getVenue().getId());
                    } else {
                        stmt.setNull(6, java.sql.Types.INTEGER);
                    }
                    
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new RuntimeException("Creating event failed, no rows affected.");
                    }
                    
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            event.setId(generatedId);
                            
                            // Save type-specific details
                            saveEventDetails(conn, event, generatedId);
                            
                            return generatedId;
                        } else {
                            throw new RuntimeException("Creating event failed, no ID obtained.");
                        }
                    }
                }
            },
            result -> onSuccess.run(),
            error -> onError.accept(error)
        );
    }

    /**
     * Saves the type-specific details for an event.
     * Called internally after the base event is saved.
     */
    private static void saveEventDetails(Connection conn, Event event, int eventId) throws SQLException {
        if (event instanceof WeddingEvent weddingEvent) {
            String sql = "INSERT INTO wedding_event (event_id, bride_name, groom_name, photographer_required) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, eventId);
                stmt.setString(2, weddingEvent.getBrideName());
                stmt.setString(3, weddingEvent.getGroomName());
                stmt.setInt(4, weddingEvent.isPhotographerRequired() ? 1 : 0);
                stmt.executeUpdate();
            }
        } else if (event instanceof BirthdayEvent birthdayEvent) {
            String sql = "INSERT INTO birthday_event (event_id, age, theme, number_of_kids) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, eventId);
                stmt.setInt(2, birthdayEvent.getAge());
                stmt.setString(3, birthdayEvent.getTheme());
                stmt.setInt(4, birthdayEvent.getNumberOfKids());
                stmt.executeUpdate();
            }
        } else if (event instanceof SeminarEvent seminarEvent) {
            String sql = "INSERT INTO seminar_event (event_id, chief_guest, speaker, topic) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, eventId);
                stmt.setString(2, seminarEvent.getChiefGuest());
                stmt.setString(3, seminarEvent.getSpeaker());
                stmt.setString(4, seminarEvent.getTopic());
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Fetches all events from the database.
     * Returns a list of EventTableRow objects for display in a table.
     * 
     * @return List of all events with their details
     * @throws SQLException if database operation fails
     */
    public static List<EventsController.EventTableRow> fetchAllEvents() throws SQLException {
        List<EventsController.EventTableRow> events = new ArrayList<>();

        String sql = """
                SELECT
                    e.event_id,
                    e.name AS event_name,
                    e.event_type,
                    e.time AS event_time,
                    c.client_id,
                    c.name AS client_name,
                    c.email_address AS client_email,
                    c.phone_number AS client_phone,
                    e.attendance,
                    v.venue_id,
                    v.name AS venue_name,
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
                LEFT JOIN venues v ON e.venue_id = v.venue_id
                LEFT JOIN wedding_event w ON e.event_id = w.event_id
                LEFT JOIN birthday_event b ON e.event_id = b.event_id
                LEFT JOIN seminar_event s ON e.event_id = s.event_id
                ORDER BY e.event_id DESC
                """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("event_id");
                String name = rs.getString("event_name");
                String type = rs.getString("event_type");
                String eventTime = rs.getString("event_time");
                
                // Split datetime into date and time parts
                String[] dateTimeParts = eventTime != null ? eventTime.split(" ") : new String[]{"", ""};
                String date = dateTimeParts.length > 0 ? dateTimeParts[0] : "";
                String time = dateTimeParts.length > 1 ? dateTimeParts[1] : "";
                
                // Client details
                int clientId = rs.getInt("client_id");
                String clientName = rs.getString("client_name");
                String clientEmail = rs.getString("client_email");
                String clientPhone = rs.getString("client_phone");
                
                int attendance = rs.getInt("attendance");
                
                // Venue details
                Integer venueId = rs.getInt("venue_id");
                if (rs.wasNull()) venueId = null;
                String venueName = rs.getString("venue_name");

                // Wedding details
                String brideName = rs.getString("bride_name");
                String groomName = rs.getString("groom_name");
                Integer photographerRequiredInt = rs.getInt("photographer_required");
                Boolean photographerRequired = rs.wasNull() ? null : (photographerRequiredInt == 1);

                // Birthday details
                Integer age = rs.getInt("age");
                if (rs.wasNull()) age = null;
                String theme = rs.getString("theme");
                Integer numberOfKids = rs.getInt("number_of_kids");
                if (rs.wasNull()) numberOfKids = null;

                // Seminar details
                String chiefGuest = rs.getString("chief_guest");
                String speaker = rs.getString("speaker");
                String topic = rs.getString("topic");

                events.add(new EventsController.EventTableRow(
                        id, name, type, date, time, 
                        clientId, clientName, clientEmail, clientPhone,
                        attendance, venueId, venueName,
                        brideName, groomName, photographerRequired, 
                        age, theme, numberOfKids, 
                        chiefGuest, speaker, topic
                ));
            }
        }

        return events;
    }

    /**
     * Deletes an event by ID asynchronously.
     * Due to CASCADE delete in the schema, this also removes type-specific details.
     * 
     * @param eventId The ID of the event to delete
     * @param onSuccess Called when deletion succeeds
     * @param onError Called with the exception if deletion fails
     */
    public static void deleteById(int eventId, Runnable onSuccess, Consumer<Throwable> onError) {
        String sql = "DELETE FROM events WHERE event_id = ?";

        DatabaseHelper.runAsyncUpdate(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, eventId);
                    stmt.executeUpdate();
                }
            },
            onSuccess,
            error -> onError.accept(error)
        );
    }

    /**
     * Updates an existing event in the database asynchronously.
     * 
     * @param event The event with updated values
     * @param onSuccess Called when the event is updated successfully
     * @param onError Called with the exception if update fails
     */
    public static void update(Event event, Runnable onSuccess, Consumer<Throwable> onError) {
        String sql = "UPDATE events SET name = ?, time = ?, attendance = ?, event_type = ?, venue_id = ? WHERE event_id = ?";

        DatabaseHelper.runAsync(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setString(1, event.getName());
                    stmt.setString(2, event.getFormattedDateTime());
                    stmt.setInt(3, event.getAttendance());
                    stmt.setString(4, event.getEventType());
                    
                    if (event.getVenue() != null) {
                        stmt.setInt(5, event.getVenue().getId());
                    } else {
                        stmt.setNull(5, java.sql.Types.INTEGER);
                    }
                    
                    stmt.setInt(6, event.getId());
                    stmt.executeUpdate();
                    
                    // Update type-specific details
                    updateEventDetails(conn, event);
                    
                    return event.getId();
                }
            },
            result -> onSuccess.run(),
            error -> onError.accept(error)
        );
    }

    /**
     * Updates the type-specific details for an event.
     */
    private static void updateEventDetails(Connection conn, Event event) throws SQLException {
        int eventId = event.getId();
        
        if (event instanceof WeddingEvent weddingEvent) {
            String sql = "UPDATE wedding_event SET bride_name = ?, groom_name = ?, photographer_required = ? WHERE event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, weddingEvent.getBrideName());
                stmt.setString(2, weddingEvent.getGroomName());
                stmt.setInt(3, weddingEvent.isPhotographerRequired() ? 1 : 0);
                stmt.setInt(4, eventId);
                stmt.executeUpdate();
            }
        } else if (event instanceof BirthdayEvent birthdayEvent) {
            String sql = "UPDATE birthday_event SET age = ?, theme = ?, number_of_kids = ? WHERE event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, birthdayEvent.getAge());
                stmt.setString(2, birthdayEvent.getTheme());
                stmt.setInt(3, birthdayEvent.getNumberOfKids());
                stmt.setInt(4, eventId);
                stmt.executeUpdate();
            }
        } else if (event instanceof SeminarEvent seminarEvent) {
            String sql = "UPDATE seminar_event SET chief_guest = ?, speaker = ?, topic = ? WHERE event_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, seminarEvent.getChiefGuest());
                stmt.setString(2, seminarEvent.getSpeaker());
                stmt.setString(3, seminarEvent.getTopic());
                stmt.setInt(4, eventId);
                stmt.executeUpdate();
            }
        }
    }

    // ==================== Statistics Methods ====================
    // These methods calculate counts live from the database

    /**
     * Gets the total count of all events.
     */
    public static int getTotalEventsCount() {
        return getCount("SELECT COUNT(*) FROM events");
    }

    /**
     * Gets the count of wedding events.
     */
    public static int getWeddingEventsCount() {
        return getCount("SELECT COUNT(*) FROM events WHERE event_type = 'Wedding'");
    }

    /**
     * Gets the count of birthday events.
     */
    public static int getBirthdayEventsCount() {
        return getCount("SELECT COUNT(*) FROM events WHERE event_type = 'Birthday'");
    }

    /**
     * Gets the count of seminar events.
     */
    public static int getSeminarEventsCount() {
        return getCount("SELECT COUNT(*) FROM events WHERE event_type = 'Seminar'");
    }

    /**
     * Helper method to execute a COUNT query and return the result.
     */
    private static int getCount(String sql) {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
