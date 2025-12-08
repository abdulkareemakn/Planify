package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.example.planifyfx.model.Venue;
import org.example.planifyfx.util.DatabaseHelper;

/**
 * Repository class for Venue database operations.
 * Handles saving and retrieving venue information.
 */
public class VenueRepository {
    
    /**
     * Saves a venue to the database asynchronously.
     * After saving, the venue's ID is set to the generated database ID.
     * 
     * @param venue The venue to save
     * @param onSuccess Called with the generated venue ID when save succeeds
     * @param onError Called with the exception if save fails
     */
    public static void save(Venue venue, Consumer<Integer> onSuccess, Consumer<Throwable> onError) {
        String sql = "INSERT INTO venues (name, address, capacity) VALUES (?, ?, ?)";

        DatabaseHelper.runAsync(
            () -> {
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    
                    stmt.setString(1, venue.getName());
                    stmt.setString(2, venue.getAddress());
                    stmt.setInt(3, venue.getCapacity());
                    
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new RuntimeException("Creating venue failed, no rows affected.");
                    }
                    
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            venue.setId(generatedId);
                            return generatedId;
                        } else {
                            throw new RuntimeException("Creating venue failed, no ID obtained.");
                        }
                    }
                }
            },
            result -> onSuccess.accept((Integer) result),
            error -> onError.accept(error)
        );
    }

    /**
     * Fetches all venues from the database asynchronously.
     * 
     * @param onSuccess Called with the list of venues when fetch succeeds
     * @param onError Called with the exception if fetch fails
     */
    public static void fetchAll(Consumer<List<Venue>> onSuccess, Consumer<Throwable> onError) {
        String sql = "SELECT venue_id, name, address, capacity FROM venues ORDER BY name";

        DatabaseHelper.runAsync(
            () -> {
                List<Venue> venues = new ArrayList<>();
                
                try (Connection conn = DatabaseHelper.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    
                    while (rs.next()) {
                        Venue venue = new Venue();
                        venue.setId(rs.getInt("venue_id"));
                        venue.setName(rs.getString("name"));
                        venue.setAddress(rs.getString("address"));
                        venue.setCapacity(rs.getInt("capacity"));
                        venues.add(venue);
                    }
                }
                return venues;
            },
            result -> {
                @SuppressWarnings("unchecked")
                List<Venue> venues = (List<Venue>) result;
                onSuccess.accept(venues);
            },
            error -> onError.accept(error)
        );
    }
}
