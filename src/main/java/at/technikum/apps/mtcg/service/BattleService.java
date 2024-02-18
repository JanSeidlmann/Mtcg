package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.BattleRepository;
import at.technikum.apps.mtcg.repository.DeckRepository;

import java.util.Collections;
import java.util.List;

public class BattleService {
    // KEINE RESPONSES IN SERVICES

    private final DeckRepository deckRepository;
    private final BattleRepository battleRepository;
    StringBuilder battleLogger = new StringBuilder();



    public BattleService() {
        this.deckRepository = new DeckRepository();
        this.battleRepository = new BattleRepository();

    }

    public enum BattleOutcome {
        PLAYER1_WIN, PLAYER2_WIN, DRAW
    }

    public String startBattle(String player1, String player2) {
        List<Card> deckA = deckRepository.getDeck(player1);
        List<Card> deckB = deckRepository.getDeck(player2);

        if (player1.equals(player2))
            return "You can't fight yourself";

        battleLogger.delete(0, battleLogger.length());

        BattleOutcome result = battle(player1, player2, deckA, deckB);
        battleLogger.append("\n").append(getWinner(player1, player2, result));
        battleRepository.updateStats(player1, player2, result);

        return battleLogger.toString();
    }

    private BattleOutcome battle(String player1, String player2, List<Card> player1_deck, List<Card> player2_deck) {
        int round = 0;

        while (round < 100) {
            Card card1 = drawRandomCard(player1_deck);
            Card card2 = drawRandomCard(player2_deck);

            BattleOutcome result = calculateRoundResult(card1, card2);
            logBattleRound(player1, player2, round, card1, card2, result);

            if (result == BattleOutcome.PLAYER1_WIN) {
                player1_deck.add(card1);
                player1_deck.add(card2);
            } else if (result == BattleOutcome.PLAYER2_WIN) {
                player2_deck.add(card1);
                player2_deck.add(card2);
            } else {
                player1_deck.add(card1);
                player2_deck.add(card1);
            }

            if (player1_deck.isEmpty()) {
                return BattleOutcome.PLAYER2_WIN;
            } else if (player2_deck.isEmpty()) {
                return BattleOutcome.PLAYER1_WIN;
            }

            round++;
        }

        return BattleOutcome.DRAW;
    }

    private String getWinner(String player1, String player2, BattleOutcome result) {
        switch (result) {
            case PLAYER1_WIN:
                return player1 + " wins";
            case PLAYER2_WIN:
                return player2 + " wins";
            default:
                return "It's a draw";
        }
    }


    private boolean isMonster(Card card) {
        return card.getName().toLowerCase().contains("spell");
    }

    private boolean isSpell(Card card) {
        return !isMonster(card);
    }

    private BattleOutcome calculateRoundResult(Card card1, Card card2) {
        if (isMonster(card1) && isMonster(card2)) {
            return calculateMonsterRoundResult(card1, card2);
        } else {
            return calculateMixedRoundResult(card1, card2);
        }
    }

    private BattleOutcome calculateMonsterRoundResult(Card monster1, Card monster2) {
        // Implement monster-specific rules

        // Placeholder logic for now, where the one with higher damage wins
        int damage1 = monster1.getDamage();
        int damage2 = monster2.getDamage();

        if (damage1 > damage2) {
            return BattleOutcome.PLAYER1_WIN;
        } else if (damage1 < damage2) {
            return BattleOutcome.PLAYER2_WIN;
        } else {
            return BattleOutcome.DRAW;
        }
    }

    private BattleOutcome calculateMixedRoundResult(Card card1, Card card2) {
        if (isSpell(card1) || isSpell(card2)){
            if (card1.getType().equals("Fire") && card2.getType().equals("Regular")){
                int newDamage1 = card1.getDamage()*2;
                card1.setDamage(newDamage1);
                int newDamage2 = card2.getDamage()/2;
                card2.setDamage(newDamage2);
            } else if (card1.getType().equals("Water") && card2.getType().equals("Fire")){
                int newDamage1 = card1.getDamage()*2;
                card1.setDamage(newDamage1);
                int newDamage2 = card2.getDamage()/2;
                card2.setDamage(newDamage2);
            } else if (card1.getType().equals("Regular") && card2.getType().equals("Water")){
                int newDamage1 = card1.getDamage()*2;
                card1.setDamage(newDamage1);
                int newDamage2 = card2.getDamage()/2;
                card2.setDamage(newDamage2);
            } else if (card1.getType().equals(card2.getType())) {
                int newDamage1 = card1.getDamage()*0;
                card1.setDamage(newDamage1);
                int newDamage2 = card2.getDamage()*0;
                card2.setDamage(newDamage2);
            }

        }
        // Placeholder logic for now, where the one with higher damage wins
        int card1Damage = card1.getDamage();
        int card2Damage = card2.getDamage();

        if (card1Damage > card2Damage) {
            return BattleOutcome.PLAYER1_WIN;
        } else if (card1Damage < card2Damage) {
            return BattleOutcome.PLAYER2_WIN;
        } else {
            return BattleOutcome.DRAW;
        }
    }

    private Card drawRandomCard(List<Card> deck) {
        if (!deck.isEmpty()) {
            Collections.shuffle(deck);
            return deck.remove(0);
        } else {
            return null;
        }
    }

    private void logBattleRound(String player1, String player2, int round, Card card1, Card card2, BattleOutcome result) {
        String logEntry = String.format("Round %d: Player 1 (%s) vs. Player 2 (%s) - %s",
                round, card1.getName(), card2.getName(), getResultString(result));

        battleLogger.append(logEntry).append("\n");
    }

    private String getResultString(BattleOutcome result) {
        switch (result) {
            case PLAYER1_WIN:
                return "Player 1 wins";
            case PLAYER2_WIN:
                return "Player 2 wins";
            default:
                return "Draw";
        }
    }
}
