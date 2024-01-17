package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckRepository {
    private final String SELECT_DECK = "SELECT card_id FROM deck WHERE username = ? LIMIT 4";
    private final String CONFIGURE_DECK = "UPDATE deck SET card_id = ? WHERE username = ? and card_id = ?";
    private final String CARD_FROM_ID = "SELECT * FROM cards WHERE id = ?";
    private final String EMPTY_DECK = "INSERT INTO deck (username, card_id) VALUES (?, ?)";
    private final String USER_OWNS = "SELECT COUNT(*) FROM bought WHERE username = ? AND card_id = ?";
    private final Database database = new Database();

    public List<Card> getDeck(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_DECK);
        ) {
            pstmt.setString(1, username);

            List<Card> userDeck = new ArrayList<>();

            try (ResultSet cardResultSet = pstmt.executeQuery()) {
                while (cardResultSet.next()) {
                    String cardId = cardResultSet.getString("card_id");

                    Card card = createCardFromId(cardId);
                    userDeck.add(card);
                }
            }

            return userDeck;

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user deck", e);
        }
    }

    public boolean userOwnsCard(String username, String cardId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(USER_OWNS);
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, cardId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking card ownership in the database", e);
        }

        return false;
    }

    public void configureDeck(String username, List<String> cardIds) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt1 = con.prepareStatement(CONFIGURE_DECK);
                PreparedStatement pstmt2 = con.prepareStatement(EMPTY_DECK);
        ) {
            List<String> existingCardIds = getDeckIds(username);

            if (!existingCardIds.isEmpty()) {
                if (existingCardIds.size() != cardIds.size()) {
                    throw new RuntimeException("Invalid deck configuration");
                }

                // Iteriere über beide Listen und ersetze jede vorhandene card_id durch die entsprechende neue card_id
                for (int i = 0; i < existingCardIds.size(); i++) {
                    String existingId = existingCardIds.get(i);
                    String cardId = cardIds.get(i);

                    pstmt1.setString(1, cardId);
                    pstmt1.setString(2, username);
                    pstmt1.setString(3, existingId);
                    pstmt1.executeUpdate();
                }
            } else {
                // Wenn das Deck leer ist, füge die neuen Karten hinzu
                for (String cardId : cardIds) {
                    pstmt2.setString(1, username);
                    pstmt2.setString(2, cardId);
                    pstmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error configuring user deck", e);
        }
    }

    private Card createCardFromId(String cardId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CARD_FROM_ID);
        ) {
            pstmt.setString(1, cardId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    Card card = new Card();
                    card.setId(resultSet.getString("id"));
                    card.setName(resultSet.getString("name"));
                    card.setDamage(resultSet.getInt("damage"));
                    card.setType(resultSet.getString("type"));

                    return card;
                } else {
                    throw new RuntimeException("Card not found with ID: " + cardId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving card from database", e);
        }
    }

    public List<String> getDeckIds(String username) {
        List<String> deckIds = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_DECK);
        ) {
            pstmt.setString(1, username);

            try (ResultSet cardResultSet = pstmt.executeQuery()) {
                while (cardResultSet.next()) {
                    String cardId = cardResultSet.getString("card_id");
                    deckIds.add(cardId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving deck IDs", e);
        }

        return deckIds;
    }


}
