package com.jjurm.projects.mpp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

  private static Connection connection;

  public static void connect() throws SQLException {
    connection = DriverManager.getConnection("jdbc:mysql://localhost/immc?user=root"
        + "&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
  }

  public static void close() throws SQLException {
    connection.close();
    connection = null;
  }

  public static Connection getConnection() throws SQLException {
    return connection;
  }

}
