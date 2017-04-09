package com.jjurm.projects.mpp.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimeZone;

import com.jjurm.projects.mpp.db.DatabaseManager;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.Point;

public class PlaceFinder {

  public static final String QUERY_BASE =
      "SELECT id, country, accent, lat, lon, tz_id FROM bigcities";

  public static Place id(int id) {
    return query("id = " + id);
  }

  public static Place city(String city) {
    return query("city like '%" + city + "%'");
  }

  public static Place accent(String accent) {
    return query("accent like '%" + accent + "%'");
  }

  public static Place fromResultSet(ResultSet result) throws SQLException {
    int id = result.getInt("id");
    String name = result.getString("accent");
    Point point = new Point(new DegreeCoordinate(result.getDouble("lat")),
        new DegreeCoordinate(result.getDouble("lon")));
    TimeZone timezone = TimeZone.getTimeZone(result.getString("tz_id"));
    Place place = new Place(id, name, point, timezone);
    return place;
  }

  protected static Place query(String condition) {
    String query = QUERY_BASE + " WHERE " + condition + " ORDER BY population DESC LIMIT 1";
    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query)) {
      if (result.first()) {
        return fromResultSet(result);
      } else {
        throw new RuntimeException("Place not found (" + condition + ")");
      }
    } catch (SQLException e1) {
      throw new RuntimeException("Place not found", e1);
    }
  }

}
