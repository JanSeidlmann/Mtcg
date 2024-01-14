package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.BattleLog;
import at.technikum.apps.mtcg.entity.BattleResult;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.DeckRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BattleService {
    // KEINE RESPONSES IN SERVICES

    private final DeckRepository deckRepository;

    public BattleService() {
        this.deckRepository = new DeckRepository();
    }

    public BattleLog startBattle(String player1, String player2) {
        List<Card> deckA = deckRepository.getDeck(player1);
        List<Card> deckB = deckRepository.getDeck(player2);

        BattleLog battleLog = new BattleLog();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Future<BattleResult>> results = new ArrayList<>();

        try {
            for (int round = 1; round <= 100; round++) {
                Future<BattleResult> futureA = executorService.submit(() -> playRound(player1, deckA, player2, deckB));
                Future<BattleResult> futureB = executorService.submit(() -> playRound(player2, deckB, player1, deckA));

                BattleResult resultA = futureA.get();
                BattleResult resultB = futureB.get();

                updateDecks(deckA, deckB, resultA);
                updateDecks(deckA, deckB, resultB);

                battleLog.addResult(resultA);
                battleLog.addResult(resultB);

                if (deckA.isEmpty() || deckB.isEmpty()) {
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        updatePlayerStats(player1, player2, battleLog);

        return battleLog;
    }

    private BattleResult playRound(String playerA, List<Card> deckA, String playerB, List<Card> deckB) {
        Card cardA = drawRandomCard(deckA);
        Card cardB = drawRandomCard(deckB);

        return executeRound(playerA, cardA, playerB, cardB);
    }

    private Card drawRandomCard(List<Card> deck) {
        if (!deck.isEmpty()) {
            Collections.shuffle(deck);
            return deck.remove(0);
        } else {
            return null;
        }
    }

    private BattleResult executeRound(String playerA, Card cardA, String playerB, Card cardB) {
        int damageA = cardA.getDamage();
        int damageB = cardB.getDamage();

        if (damageA > damageB) {
            return new BattleResult(playerA, cardA, playerB, cardB, BattleResult.Result.WIN);
        } else if (damageA < damageB) {
            return new BattleResult(playerB, cardB, playerA, cardA, BattleResult.Result.WIN);
        } else {
            return new BattleResult(playerA, cardA, playerB, cardB, BattleResult.Result.DRAW);
        }
    }

    private void updateDecks(List<Card> deckA, List<Card> deckB, BattleResult result) {
        if (result.getResult() == BattleResult.Result.WIN) {
            deckA.add(result.getWinnerCard());
            deckB.remove(result.getLoserCard());
        } else {
            deckA.remove(result.getLoserCard());
            deckB.add(result.getWinnerCard());
        }
    }

    private void updatePlayerStats(String player1, String player2, BattleLog battleLog) {
        // Update player statistics based on the battle log
        // Implement logic to process battle results and update player stats
    }
}
