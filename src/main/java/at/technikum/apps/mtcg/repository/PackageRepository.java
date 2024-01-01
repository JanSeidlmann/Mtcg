package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PackageRepository implements Repository {

    private final String SAVE_PACKAGE_SQL = "INSERT INTO packages (package_id, bought) VALUES (?, ?)";
    private final String SAVE_CARD_SQL = "INSERT INTO cards (package_id, card_id, name, damage, type, isSpell) VALUES (?, ?, ?, ?, ?, ?)";
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

                pstmt1.setInt(1, card.getPackage_id());
                pstmt1.setString(2, card.getCard_id());
                pstmt1.setString(3, card.getName());
                pstmt1.setInt(4, card.getDamage());
                pstmt1.setString(5, card.getType());
                pstmt1.setBoolean(6, card.isSpell());
                pstmt1.executeUpdate();
            }

            Card c = newCards.get(1);
            pstmt2.setInt(1, c.getPackage_id());
            pstmt2.setBoolean(2, false);
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
    public Optional<User> findUser(UUID id) {
        return Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        return null;
    }
}
