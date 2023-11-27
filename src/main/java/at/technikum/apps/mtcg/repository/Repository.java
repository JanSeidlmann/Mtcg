package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository {

    List<Card> findAllCards();

    List<User> findAllUsers();

    Optional<Card> findCard(int id);

    Optional<User> findUser(UUID id);

    Card save(Card card);

    User saveUser(User user);
}
