package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepository {

    // Funktionen für package auswahlen und bezahlen dann karten selecten von dem package dann coinsabziehen vom user. und dem user die karten geben.
    private final String ENOUGH_COINS = "SELECT coins FROM users WHERE username = ?";
    private final String DEDUCT_COINS = "UPDATE users SET coins = coins - 5 WHERE username = ?";
    private final Database database = new Database();
    public boolean enoughCoins(String username){
        try (
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(ENOUGH_COINS);
        ) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int coins = rs.getInt("coins");
                    return coins < 5;
                }
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e){
            throw new RuntimeException("Error checking coins", e);
        }
        return true;
    }

    public void deductCoins(String username){
        try (
            Connection con = database.getConnection();
            PreparedStatement pstmt = con.prepareStatement(DEDUCT_COINS);
        ) {
            if (enoughCoins(username)){
                throw new RuntimeException("Not enough coins!");
            }

            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0){
                throw new RuntimeException("Failed to deduct coins");
            }

            pstmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Error checking coins", e);
        }
    }
}
