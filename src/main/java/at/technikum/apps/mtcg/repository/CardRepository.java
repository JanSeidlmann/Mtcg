package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private final String SELECT_USER_CARDS = "SELECT * FROM cards WHERE card_id IN (SELECT card_id FROM bought WHERE username = ?) LIMIT 20";
    private final Database database = new Database();

    public List<Card> getCards(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_CARDS);
        ) {
            pstmt.setString(1, username);

            List<Card> userCards = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setCard_id(rs.getString("card_id"));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getInt("damage"));
                    card.setType(rs.getString("type"));
                    card.setSpell(rs.getBoolean("isSpell"));

                    userCards.add(card);
                }
            }

            return userCards;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user cards", e);
        }
    }
}
