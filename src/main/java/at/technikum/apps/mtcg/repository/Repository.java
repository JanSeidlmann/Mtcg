package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Repository implements CardRepository {

    private final List<Card> cardList;

    public Repository() { this.cardList = new ArrayList<>(); }

    @Override
    public Card save(Card card){
        card.setId(cardList.size() + 1);
        cardList.add(card);
        return card;
    }

    @Override
    public List<Card> findAll() {
        return cardList;
    }

    @Override
    public Optional<Card> find(int id) {
        return Optional.empty();
    }
}
