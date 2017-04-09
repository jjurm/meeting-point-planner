package com.jjurm.projects.mpp.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.TimeZone;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.TimeZoneApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.ZeroResultsException;
import com.google.maps.model.LatLng;
import com.jjurm.projects.mpp.ApiManager;

public class CitiesImporter {

  static String filename = "cities.csv";
  static int ignoreLines = 1;

  public static void main(String[] args) throws SQLException {
    DatabaseManager.connect();
    importCities();
    fetchTimezones();
    DatabaseManager.close();
  }

  public static void test(double lat, double lon) {
    GeoApiContext context = ApiManager.getContext();
    PendingResult<TimeZone> r = TimeZoneApi.getTimeZone(context, new LatLng(lat, lon));
    try {
      TimeZone tz = r.await();
      System.out.println(tz.getID());
    } catch (ZeroResultsException e) {
      System.out.println("not found");
    } catch (ApiException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("resource")
  public static void importCities() {

    PreparedStatement stmt = null;
    PreparedStatement stmt2 = null;
    String insertStatement =
        "INSERT INTO `cities`(`country`, `city`, `accent`, `region`, `population`, `lat`, `lon`) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String insert2Statement =
        "INSERT INTO `bigcities`(`country`, `city`, `accent`, `region`, `population`, `lat`, `lon`) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      Connection conn = DatabaseManager.getConnection();
      conn.setAutoCommit(false);
      stmt = conn.prepareStatement(insertStatement);
      stmt2 = conn.prepareStatement(insert2Statement);

      for (int i = 0; i < ignoreLines; i++) {
        br.readLine();
      }
      String line;
      String[] parts;
      Integer population;
      while ((line = br.readLine()) != null) {
        parts = line.split(",");

        if (parts[4].length() > 0) {
          population = Integer.parseInt(parts[4]);
        } else {
          population = null;
        }

        stmt.setString(1, parts[0]);
        stmt.setString(2, parts[1]);
        stmt.setString(3, parts[2]);
        stmt.setString(4, parts[3]);
        if (population != null) {
          stmt.setInt(5, population);
        } else {
          stmt.setNull(5, Types.INTEGER);
        }
        stmt.setDouble(6, Double.parseDouble(parts[5]));
        stmt.setDouble(7, Double.parseDouble(parts[6]));
        stmt.executeUpdate();

        if (population != null && population > 50000) {
          stmt2.setString(1, parts[0]);
          stmt2.setString(2, parts[1]);
          stmt2.setString(3, parts[2]);
          stmt2.setString(4, parts[3]);
          stmt2.setInt(5, population);
          stmt2.setDouble(6, Double.parseDouble(parts[5]));
          stmt2.setDouble(7, Double.parseDouble(parts[6]));
          stmt2.executeUpdate();
        }
      }
      conn.commit();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (stmt != null)
        try {
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      if (stmt2 != null)
        try {
          stmt2.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
    }
  }

  @SuppressWarnings("resource")
  public static void fetchTimezones() {
    PreparedStatement stmtSelect = null;
    PreparedStatement stmtUpdate = null;
    ResultSet result = null;
    GeoApiContext context = ApiManager.getContext();

    try {

      Connection conn = DatabaseManager.getConnection();
      conn.setAutoCommit(false);
      stmtSelect = conn.prepareStatement("SELECT id, lat, lon FROM bigcities");
      stmtUpdate = conn.prepareStatement("UPDATE bigcities SET tz_id = ? WHERE id = ?");
      result = stmtSelect.executeQuery();

      int i = 0;
      while (result.next()) {
        if (i % 100 == 0) {
          conn.commit();
          System.out.println(i);
        }
        PendingResult<TimeZone> r = TimeZoneApi.getTimeZone(context,
            new LatLng(result.getDouble("lat"), result.getDouble("lon")));
        try {
          TimeZone tz = r.await();
          stmtUpdate.setString(1, tz.getID());
        } catch (ZeroResultsException e) {
          stmtUpdate.setNull(1, Types.VARCHAR);
        }
        stmtUpdate.setInt(2, result.getInt("id"));
        stmtUpdate.executeUpdate();
        i++;
      }

      conn.commit();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ApiException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (result != null)
        try {
          result.close();
        } catch (SQLException e1) {
          e1.printStackTrace();
        }
      if (stmtSelect != null)
        try {
          stmtSelect.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      if (stmtUpdate != null)
        try {
          stmtUpdate.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
    }

  }

}
