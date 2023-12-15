package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository {

    List<Card> findAllCards();

    Optional<Card> findCard(int id);

    Optional<User> findUser(UUID id);

    Card save(Card card);

    User saveUser(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> updateUserByUsername(String username, Request request);
}
