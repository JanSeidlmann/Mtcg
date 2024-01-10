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
    private final String CARD_FROM_ID = "SELECT * FROM cards WHERE card_id = ?";
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

    public void configureDeck(String username, List<String> cardIds) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(CONFIGURE_DECK);
        ) {
            List<String> existingCardIds = getDeckIds(username);

            if (existingCardIds.size() != cardIds.size()) {
                throw new RuntimeException("Invalid deck configuration");
            }

            // Iteriere über beide Listen und ersetze jede existing card_id durch die entsprechende neue card_id
            for (int i = 0; i < existingCardIds.size(); i++) {
                String existingId = existingCardIds.get(i);
                String cardId = cardIds.get(i);

                pstmt.setString(1, cardId);
                pstmt.setString(2, username);
                pstmt.setString(3, existingId);
                pstmt.executeUpdate();
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
                    card.setCard_id(resultSet.getString("card_id"));
                    card.setName(resultSet.getString("name"));
                    card.setDamage(resultSet.getInt("damage"));
                    card.setType(resultSet.getString("type"));
                    card.setSpell(resultSet.getBoolean("isSpell"));

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
