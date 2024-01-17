package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepository {
    private final String ENOUGH_COINS = "SELECT coins FROM users WHERE username = ?";
    private final String DEDUCT_COINS = "UPDATE users SET coins = coins - 5 WHERE username = ?";
    private final String SELECT_FIRST_PACKAGE = "SELECT * FROM packages LIMIT 1";
    private final String INSERT_BOUGHT = "INSERT INTO bought (username, card_id) VALUES (?, ?)";
    private final String DELETE_FIRST_PACKAGE = "DELETE FROM packages WHERE package_id = (SELECT package_id FROM packages LIMIT 1)";
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
        } catch (SQLException e){
            throw new RuntimeException("Error here", e);
        }
    }

    public void insertCards(String username) {
        try (
                Connection con = database.getConnection();
                PreparedStatement selectPackageStmt = con.prepareStatement(SELECT_FIRST_PACKAGE);
                PreparedStatement insertBoughtStmt = con.prepareStatement(INSERT_BOUGHT);
        ) {
            try (ResultSet packageResultSet = selectPackageStmt.executeQuery()) {
                if (packageResultSet.next()) {
                    for (int i = 1; i <= 5; i++) {
                        String cardId = packageResultSet.getString("card" + i);

                        // Karten in die neue Tabelle 'bought' einfügen
                        insertBoughtStmt.setString(1, username);
                        insertBoughtStmt.setString(2, cardId);
                        insertBoughtStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting cards", e);
        }
    }


    public void deletePackage() {
        try (
                Connection con = database.getConnection();
                PreparedStatement deletePackageStmt = con.prepareStatement(DELETE_FIRST_PACKAGE);
        ) {
            int rowsAffected = deletePackageStmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("No packages found to delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting package", e);
        }
    }

    public void acquirePackages(String username){
        try (
                Connection con = database.getConnection();
        ) {
            System.out.println("Checking if the user has enough coins!");
            if (enoughCoins(username))
                throw new RuntimeException("Not enough coins!");

            System.out.println("Select and insert cards!");
            insertCards(username);

            System.out.println("Deleting package!");
            deletePackage();

            System.out.println("Deducting coins!");
            deductCoins(username);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error acquiring a package", e);
        }
    }
}
