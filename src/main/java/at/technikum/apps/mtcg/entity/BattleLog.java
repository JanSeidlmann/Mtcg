package at.technikum.apps.mtcg.entity;

import java.util.ArrayList;
import java.util.List;

public class BattleLog {
    private List<BattleResult> results;

    public BattleLog() {
        this.results = new ArrayList<>();
    }

    public List<BattleResult> getResults() {
        return results;
    }

    public void addResult(BattleResult result) {
        results.add(result);
    }

    // Hier kannst du weitere Methoden hinzufügen, um die Ergebnisse nach dem Battle zu verarbeiten
    public void printBattleLog() {
        for (int i = 0; i < results.size(); i++) {
            BattleResult result = results.get(i);
            System.out.println("Round " + (i + 1) + ": " + result.toString());
        }
    }
}
