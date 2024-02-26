package at.technikum.apps.mtcg.repository;
import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.service.BattleService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatsRepository {
  private final String GET_STATS = "SELECT username, totalGames, gamesWon, gamesLost, elo FROM stats WHERE username = ?";
  private final String UPDATE_STATS_DRAW = "UPDATE stats SET totalGames = totalGames + 1 WHERE username IN (?, ?)";
  private final String UPDATE_STATS_WIN = "UPDATE stats SET totalGames = totalGames + 1, gamesWon = gamesWon + 1, elo = elo + 3 WHERE username = ?; " +
          "UPDATE stats SET totalGames = totalGames + 1, gamesLost = gamesLost + 1, elo = elo - 5 WHERE username = ?;";

  Database database = new Database();
  public Stats getStats(String username) {
    try (Connection connection = database.getConnection()) {
      assert connection != null;
      try (PreparedStatement stmt = connection.prepareStatement(GET_STATS)){
        stmt.setString(1, username);

        ResultSet result = stmt.executeQuery();

        if (result.next()) {
          Stats stats = new Stats();
          stats.setUsername(result.getString("username"));
          stats.setTotalGames(result.getInt("totalGames"));
          stats.setGamesWon(result.getInt("gamesWon"));
          stats.setGamesLost(result.getInt("gamesLost"));
          stats.setElo(result.getInt("elo"));

          return stats;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateStats(String player1, String player2, BattleService.BattleOutcome result) {
    String query, winner = player1, loser = player2;

    if (result == BattleService.BattleOutcome.DRAW) {
      query = UPDATE_STATS_DRAW;
    } else {
      if (result == BattleService.BattleOutcome.PLAYER2_WIN) {
        winner = player2;
        loser = player1;
      } else {
        winner = player1;
        loser = player2;
      }
      query = UPDATE_STATS_WIN;
    }

    try (Connection connection = database.getConnection()) {
      assert connection != null;
      try (PreparedStatement stmt = connection.prepareStatement(query)){
        stmt.setString(1, winner);
        stmt.setString(2, loser);
        stmt.executeUpdate();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
