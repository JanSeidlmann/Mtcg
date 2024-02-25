package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.service.BattleService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleRepository {

    private final Database database = new Database();

    private final String UPDATE_WINNER_COINS = "UPDATE users SET coins = coins + 1 WHERE username = ?";

  public void getReward(String player2) {
      try (
        Connection con = database.getConnection();
        PreparedStatement pstmt = con.prepareStatement(UPDATE_WINNER_COINS)
      ) {
          pstmt.setString(1, player2);
          pstmt.executeUpdate();
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
}
