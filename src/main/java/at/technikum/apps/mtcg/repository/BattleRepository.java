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

//    public Stats getStats(String username) {
//        String query = "SELECT users.username, totalGames, gamesWon, gamesLost, elo FROM stats " +
//                "INNER JOIN users ON stats.username = users.username " +
//                "WHERE stats.username = ?";
//
//        try (Connection connection = database.getConnection()) {
//            assert connection != null;
//            try (PreparedStatement stmt = connection.prepareStatement(query)){
//                stmt.setString(1, username);
//
//                ResultSet result = stmt.executeQuery();
//
//                if (result.next()) {
//                    Stats stats = new Stats();
//                    stats.setUsername(result.getString("username"));
//                    stats.setTotalGames(result.getInt("totalGames"));
//                    stats.setGamesWon(result.getInt("gamesWon"));
//                    stats.setGamesLost(result.getInt("gamesLost"));
//                    stats.setElo(result.getInt("elo"));
//
//                    return stats;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public void updateStats(String player1, String player2, BattleService.BattleOutcome result) {
        String query, winner = player1, loser = player2;

        if (result == BattleService.BattleOutcome.DRAW) {
            query = "UPDATE stats SET totalGames = totalGames + 1 WHERE username IN (?, ?)";
        } else {
            if (result == BattleService.BattleOutcome.PLAYER2_WIN) {
                winner = player2;
                loser = player1;
            }
            query = "UPDATE stats SET totalGames = totalGames + 1, gamesWon = gamesWon + 1, elo = elo + 3 WHERE username = ?; " +
                    "UPDATE stats SET totalGames = totalGames + 1, gamesLost = gamesLost + 1, elo = elo - 5 WHERE username = ?;";
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
