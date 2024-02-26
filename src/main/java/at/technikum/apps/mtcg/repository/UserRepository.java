package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements Repository {
    private final String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final String UPDATE_USER_BY_USERNAME = "UPDATE users SET username = ?, Bio = ?, Image = ? WHERE username = ?";
    private final String SAVE_SQL = "INSERT INTO users(id, Username, Password, coins) VALUES(?, ?, ?, ?)";
    private final Database database = new Database();

    @Override
    public User saveUser(User user) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_SQL)
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, 20);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void addCards(List<Card> newCards) {
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(FIND_USER_BY_USERNAME)
        ) {
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String password = rs.getString("password");
                    int coins = rs.getInt("coins");

                    User user = new User();
                    user.setId(id);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setCoins(coins);

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> updateUserByUsername(String username, Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (
                Connection con = database.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_BY_USERNAME)
        ) {
            String newUsername = objectMapper.readTree(request.getBody()).get("Username").asText();
            String newBio = objectMapper.readTree(request.getBody()).get("Bio").asText();
            String newImage = objectMapper.readTree(request.getBody()).get("Image").asText();

            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, newBio);
            preparedStatement.setString(3, newImage);
            preparedStatement.setString(4, username);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // If the update was successful, retrieve the updated user
                return findUserByUsername(newUsername);
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
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

}
