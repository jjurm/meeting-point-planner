package com.jjurm.projects.mpp.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class CitiesImporter {

  static String filename = "cities.csv";
  static int ignoreLines = 1;

  public static void main(String[] args) throws SQLException {
    DatabaseManager.connect();
    importCities();
    DatabaseManager.close();
  }

  public static void importCities() {

    PreparedStatement stmt = null;
    String insertStatement =
        "INSERT INTO `cities`(`country`, `city`, `accent`, `region`, `population`, `lat`, `lon`) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      Connection conn = DatabaseManager.getConnection();
      conn.setAutoCommit(false);
      stmt = conn.prepareStatement(insertStatement);

      for (int i = 0; i < ignoreLines; i++) {
        br.readLine();
      }
      String line;
      String[] parts;
      while ((line = br.readLine()) != null) {
        parts = line.split(",");

        stmt.setString(1, parts[0]);
        stmt.setString(2, parts[1]);
        stmt.setString(3, parts[2]);
        stmt.setString(4, parts[3]);
        if (parts[4].length() > 0) {
          stmt.setInt(5, Integer.parseInt(parts[4]));
        } else {
          stmt.setNull(5, Types.INTEGER);
        }
        stmt.setDouble(6, Double.parseDouble(parts[5]));
        stmt.setDouble(7, Double.parseDouble(parts[6]));
        stmt.executeUpdate();
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
    }
  }

}
