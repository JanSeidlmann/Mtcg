package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DatabaseRepository implements Repository {

    private final List<Card> cardList;
    private final List<User> userList;
    private final String FIND_ALL_SQL = "SELECT * FROM users";
    private final String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final String SAVE_SQL = "INSERT INTO users(id, username, password) VALUES(?, ?, ?)";
    private final Database database = new Database();
    public DatabaseRepository(List<Card> cardList, List<User> userList) {
        this.cardList = cardList;
        this.userList = userList;
    }

    @Override
    public User saveUser(User user) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // THOUGHT: how do i handle exceptions (hint: look at the TaskApp)
        }

        return user;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (
            Connection con = database.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(FIND_USER_BY_USERNAME);
            ResultSet rs = preparedStatement.executeQuery("SELECT * FROM users WHERE username = ?");
        ) {
            preparedStatement.setString(1, username);

                if (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");

                    User user = new User();
                    user.setId(id);
                    user.setUsername(username);
                    user.setPassword(password);

                    return Optional.of(user);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Fehlt noch
        }

        return Optional.empty();
    }

    @Override
    public List<Card> findAllCards() {
        return null;
    }

    @Override
    public Optional<Card> findCard(int id) {
        return Optional.empty();
    }


    @Override
    public Optional<User> findUser(UUID id) {
        return Optional.empty();
    }

    @Override
    public Card save(Card card) {
        return null;
    }
}
