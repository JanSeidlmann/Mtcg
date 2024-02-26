package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository {

    List<Card> findAllCards();

    Optional<Card> findCard(int id);

    Optional<User> findUser(UUID id);

    User saveUser(User user);

    void addCards(List<Card> newCards);

    Optional<User> findUserByUsername(String username);

    Optional<User> updateUserByUsername(String username, Request request);

  void setUpStats(User user);
}
