package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradingRepository {

    private final String SAVE_TRADE_SQL = "INSERT INTO trade (trade_id, card_id, type, damage, sellerUsername) VALUES (?, ?, ?, ?, ?)";
    private final String SELECT_USER_CARDS = "SELECT card_id FROM cards";
    private final String SELECT_USER_DECK = "SELECT card_id FROM deck WHERE username = ?";
    private final String UPDATE_USER_BOUGHT_CARD_SQL = "UPDATE bought SET card_id = ? WHERE username = ? AND card_id = ?";
    private final String SELECT_TRADE_DETAILS_SQL = "SELECT card_id, sellerUsername FROM trade WHERE trade_id = ?";
    private final String SELECT_USER_TRADES = "SELECT * FROM trade";
    private final String DELETE_TRADE = "DELETE FROM trade WHERE trade_id = ?";

    private final Database database = new Database();

    private final PackageService packageService;

    public TradingRepository() {
        this.packageService = new PackageService();
    }

    public void tradeCards(String tradeId, String buyerUsername, String card_id_buyer) {
        try (
                Connection con = database.getConnection();
                PreparedStatement updateCardStmt = con.prepareStatement(UPDATE_USER_BOUGHT_CARD_SQL);
                PreparedStatement selectCardIdFromTrade = con.prepareStatement(SELECT_TRADE_DETAILS_SQL);
        ) {
            // Hole die Details des Trades aus der Datenbank
            selectCardIdFromTrade.setString(1, tradeId);
            try (ResultSet tradeResult = selectCardIdFromTrade.executeQuery()) {
                if (!tradeResult.next()) {
                    throw new RuntimeException("Trade not found");
                }

                String card_id_seller = tradeResult.getString("card_id");
                String sellerUsername = tradeResult.getString("sellerUsername");

                // Aktualisiere die Karte des Verkäufers im Deck des Käufers
                updateCardStmt.setString(1, card_id_buyer);
                updateCardStmt.setString(2, sellerUsername);
                updateCardStmt.setString(3, card_id_seller);
                updateCardStmt.executeUpdate();


                // Aktualisiere die Karte des Käufers im Deck des Verkäufers
                updateCardStmt.setString(1, card_id_seller);
                updateCardStmt.setString(2, buyerUsername);
                updateCardStmt.setString(3, card_id_buyer);
                updateCardStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error trading cards", e);
        }
    }

    public List<String> getCards() {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_CARDS);
        ) {

            List<String> userCards = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("card_id");
                    userCards.add(cardId);
                }
            }
            return userCards;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user cards", e);
        }
    }

    public List<String> getDeck(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_DECK);
        ) {
            pstmt.setString(1, username);

            List<String> userDeck = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("card_id");
                    userDeck.add(cardId);
                }
            }
            return userDeck;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user cards", e);
        }
    }

    public List<Trade> getTrades() {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SELECT_USER_TRADES);
        ) {
            List<Trade> userTrades = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Trade trade = new Trade();
                    trade.setTrade_id(rs.getString("trade_id"));
                    trade.setCard_id(rs.getString("card_id"));
                    trade.setType(rs.getString("type"));
                    trade.setDamage(rs.getInt("damage"));
                    userTrades.add(trade);
                }
            }
            return userTrades;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user trades", e);
        }
    }


    public void createTrade(Request request, String trade_id, String cardId, String type, int damage){
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_TRADE_SQL);
        ) {
            String token = request.getToken();
            String sellerUsername = packageService.extractUsernameFromToken(token);

            pstmt.setString(1, trade_id);
            pstmt.setString(2, cardId);
            pstmt.setString(3, type);
            pstmt.setInt(4, damage);
            pstmt.setString(5, sellerUsername);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTrade(String tradeId, String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement selectTradeStmt = con.prepareStatement("SELECT card_id, sellerUsername FROM trade WHERE trade_id = ?");
                PreparedStatement deleteTradeStmt = con.prepareStatement("DELETE FROM trade WHERE trade_id = ?");
        ) {
            // Überprüfe, ob der Trade existiert und erhalte Informationen über die Karte und den Verkäufer
            selectTradeStmt.setString(1, tradeId);
            try (ResultSet tradeResult = selectTradeStmt.executeQuery()) {
                if (tradeResult.next()) {
                    String cardId = tradeResult.getString("card_id");
                    String sellerUsername = tradeResult.getString("sellerUsername");

                    // Überprüfe, ob der Benutzer die Karte besitzt
                    if (username.equals(sellerUsername) || userHasCard(username, cardId)) {
                        deleteTradeStmt.setString(1, tradeId);
                        deleteTradeStmt.executeUpdate();
                    } else {
                        throw new RuntimeException("User is not authorized to delete this trade");
                    }
                } else {
                    throw new RuntimeException("Trade not found");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting trade", e);
        }
    }

    public boolean userHasCard(String username, String cardId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement selectCardStmt = con.prepareStatement("SELECT COUNT(*) FROM bought WHERE username = ? AND card_id = ?");
        ) {
            selectCardStmt.setString(1, username);
            selectCardStmt.setString(2, cardId);

            try (ResultSet resultSet = selectCardStmt.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);

                return count > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if user has card", e);
        }
    }

}
