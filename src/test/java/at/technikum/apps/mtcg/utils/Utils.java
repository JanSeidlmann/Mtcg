package at.technikum.apps.mtcg.utils;
import at.technikum.apps.mtcg.database.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
  private static final Database database = new Database();

  public static void cleanDatabase() {
    try (Connection connection = database.getConnection();
         Statement statement = connection.createStatement()) {

      String deleteCardsQuery = "DELETE FROM users";
      statement.executeUpdate(deleteCardsQuery);

    } catch (SQLException e) {
      throw new RuntimeException("Error cleaning database", e);
    }
  }
}
