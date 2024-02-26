package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PackageRepository implements Repository {

    private final String SAVE_PACKAGE_SQL = "INSERT INTO packages (card1, card2, card3, card4, card5) VALUES (?, ?, ? ,? ,?)";
    private final String SAVE_CARD_SQL = "INSERT INTO cards (Id, Name, Damage, Type) VALUES (?, ?, ?, ?)";
    private final Database database = new Database();

    @Override
    public List<Card> findAllCards() {
        return null;
    }

    @Override
    public Optional<Card> findCard(int id) {
        return Optional.empty();
    }

    public void addCards(List<Card> newCards){
        try (
            Connection con = database.getConnection();
            PreparedStatement pstmt1 = con.prepareStatement(SAVE_CARD_SQL);
            PreparedStatement pstmt2 = con.prepareStatement(SAVE_PACKAGE_SQL);
        ) {
            for (int i = 0; i < newCards.size(); i++){
                Card card = newCards.get(i);

                pstmt1.setString(1, card.getId());
                pstmt1.setString(2, card.getName());
                pstmt1.setInt(3, card.getDamage());
                pstmt1.setString(4, card.extractTypeFromName());
                pstmt1.executeUpdate();
            }

            for (int i = 0; i < newCards.size(); i++) {
                pstmt2.setString(i + 1, newCards.get(i).getId());
            }
            pstmt2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUserByUsername(String username, Request request) {
        return Optional.empty();
    }

    @Override
    public void setUpStats(User user) {
    }

    @Override
    public Optional<User> findUser(UUID id) {
        return Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
