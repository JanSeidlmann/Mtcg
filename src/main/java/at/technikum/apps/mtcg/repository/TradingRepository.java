package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradingRepository {

    private final String SAVE_TRADE_SQL = "INSERT INTO trade (trade_id, card_id, type, damage) VALUES (?, ?, ?, ?)";
    private final String SELECT_USER_CARDS = "SELECT card_id FROM cards";
    private final String SELECT_USER_DECK = "SELECT card_id FROM deck WHERE username = ?";
    private final Database database = new Database();

    public List<String> getCards() {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_CARDS);
        ) {

            List<String> userCards = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("card_id");
                    userCards.add(cardId);
                }
            }
            return userCards;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user cards", e);
        }
    }

    public List<String> getDeck(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_DECK);
        ) {
            pstmt.setString(1, username);

            List<String> userDeck = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("card_id");
                    userDeck.add(cardId);
                }
            }
            return userDeck;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user cards", e);
        }
    }

    public void createTrade(String trade_id, String cardId, String type, int damage){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_TRADE_SQL);
        ) {
            pstmt.setString(1, trade_id);
            pstmt.setString(2, cardId);
            pstmt.setString(3, type);
            pstmt.setInt(4, damage);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
