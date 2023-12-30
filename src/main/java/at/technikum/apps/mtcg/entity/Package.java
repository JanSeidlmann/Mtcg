package at.technikum.apps.mtcg.entity;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private List<Card> cards;
    private int cost;

    public Package() {
        this.cards = new ArrayList<>();
    }

    public void addCards(List<Card> newCards) {
        for (Card newCard : newCards) {
            if (cards.contains(newCard)) {
                throw new IllegalArgumentException("Duplicate card found in the package.");
            }
        }
        cards.addAll(newCards);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
