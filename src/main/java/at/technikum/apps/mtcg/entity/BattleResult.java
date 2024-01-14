package at.technikum.apps.mtcg.entity;

public class BattleResult {
    public enum Result {
        WIN,
        LOOSE,
        DRAW
    }

    private String winnerPlayer;
    private Card winnerCard;
    private String loserPlayer;
    private Card loserCard;
    private Result result;

    public BattleResult(String winnerPlayer, Card winnerCard, String loserPlayer, Card loserCard, Result result) {
        this.winnerPlayer = winnerPlayer;
        this.winnerCard = winnerCard;
        this.loserPlayer = loserPlayer;
        this.loserCard = loserCard;
        this.result = result;
    }

    public String getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setWinnerPlayer(String winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public Card getWinnerCard() {
        return winnerCard;
    }

    public void setWinnerCard(Card winnerCard) {
        this.winnerCard = winnerCard;
    }

    public String getLoserPlayer() {
        return loserPlayer;
    }

    public void setLoserPlayer(String loserPlayer) {
        this.loserPlayer = loserPlayer;
    }

    public Card getLoserCard() {
        return loserCard;
    }

    public void setLoserCard(Card loserCard) {
        this.loserCard = loserCard;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
