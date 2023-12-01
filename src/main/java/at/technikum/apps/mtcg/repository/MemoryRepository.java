package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemoryRepository implements Repository {

    private final List<Card> cardList;

    private final List<User> userList;

    private final String SAVE_SQL = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";

    private final Database database = new Database();

    public MemoryRepository() {
        this.cardList = new ArrayList<>();
        this.userList = new ArrayList<>();
    }

    @Override
    public Card save(Card card){
        card.setId(UUID.randomUUID().toString());
        cardList.add(card);
        return card;
    }

    @Override
    public List<Card> findAllCards() {
        return cardList;
    }

    @Override
    public Optional<Card> findCard(int id) {
        return Optional.empty();
    }

    @Override
    public User saveUser(User user) {
        user.setId(UUID.randomUUID().toString());
        userList.add(user);

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.execute();
        } catch (SQLException e) {
            // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }

        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userList;
    }

    @Override
    public Optional<User> findUser(UUID id) {
        return Optional.empty();
    }
}
