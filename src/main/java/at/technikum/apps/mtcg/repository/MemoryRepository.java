package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemoryRepository implements Repository {

    private final List<Card> cardList;

    private final List<User> userList;

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
