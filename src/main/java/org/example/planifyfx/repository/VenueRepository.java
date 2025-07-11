package org.example.planifyfx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.planifyfx.model.Venue;
import org.example.planifyfx.util.DatabaseUtil;

public class VenueRepository {
//  public void save(Venue venue) {
//
//
//
//
//    String sql = "INSERT INTO venues(name, address, capacity, base_price, square_feet, is_outdoor, "
//                 + "has_restrooms) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//    try (Connection connection = DatabaseUtil.getConnection();
//        PreparedStatement statement = connection.prepareStatement(sql)
//
//    ) {
//      statement.setString(1, venue.getName());
//      statement.setString(2, venue.getAddress());
//      statement.setInt(3, venue.getCapacity());
//      statement.setDouble(4, venue.getBasePrice());
//      statement.setDouble(5, venue.getSquareFeet());
//      statement.setBoolean(6, venue.isOutdoor());
//      statement.setBoolean(7, venue.isHasRestrooms());
//      statement.executeUpdate();
//    }
//
//    catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
}
