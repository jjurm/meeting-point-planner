package com.jjurm.projects.mpp.map;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.jjurm.projects.mpp.db.DatabaseManager;
import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class DaylightMap extends ProductivityMap {

  public static final double D = 100.0 / 121.0;
  public static final double WORKING_HOURS = 16;

  public DaylightMap(Date date, Attendant attendant) {
    super(date, attendant);
  }

  @Override
  public double calculateProductivity(Place destination, int day) {
    double phi = destination.getPoint().getLatitude() * Math.PI / 180;

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR) + day;
    double declination = DeclinationCache.getDeclination(dayOfYear) * Math.PI / 180;

    double h = Math.acos(-Math.tan(phi) * Math.tan(declination)) / (2 * Math.PI);
    double sunrise = 24 * -h + 12;
    double sunset = 24 * h + 12;

    double RL = Math.max(0, -sunset + sunrise + WORKING_HOURS) / WORKING_HOURS;

    double P = RL * D + (1 - RL);

    return P;
  }

  static class DeclinationCache {

    private static HashMap<Integer, Double> cache = new HashMap<Integer, Double>();

    public static Double getDeclination(int dayOfYear) {

      if (cache.containsKey(dayOfYear)) {
        return cache.get(dayOfYear);
      }

      String query = "SELECT declination FROM sundeclination WHERE `day` = " + dayOfYear;
      try (Connection conn = DatabaseManager.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet result = stmt.executeQuery(query);) {
        if (result.first()) {
          Double declination = result.getDouble(1);
          cache.put(dayOfYear, declination);
          return declination;
        } else {
          return null;
        }
      } catch (SQLException e) {
        e.printStackTrace();
        return null;
      }
    }

  }

}
