package at.technikum.apps.mtcg.repository;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionRepositoryTest {
  private TransactionRepository transactionRepository;
  private PackageRepository packageRepository;
  private CardRepository cardRepository;

  @BeforeEach
  void cleanDatabase(){
    this.transactionRepository = mock(new TransactionRepository());
    this.packageRepository = mock(new PackageRepository());
    this.cardRepository = new CardRepository();
    Utils.cleanDatabase();
  }

  @Test
  void testDeletePackage() throws SQLException {
    // Arrange
    List<Card> cards = new ArrayList<>();
//    for (int i = 0; i < 5; i++) {
//      Card card = new Card();
//      card.setName("Fire");
//      card.setId("card" + i);
//      cards.add(card);
//    }

    // Act
    packageRepository.addCards(cards);
    transactionRepository.deletePackage();

    // Assert
    verify(transactionRepository).deletePackage();

  }

  @Test
  void enoughCoinsValid(){
    // Arrange
    User testUser = new User();
    testUser.setUsername("testuser");
    testUser.setCoins(10);

    // Act
    boolean result = transactionRepository.enoughCoins(testUser.getUsername());

    // Assert
    assertTrue(result);
  }

  @Test
  void enoughCoinsInalid(){
    // Arrange
    User testUser = new User();
    testUser.setUsername("testuser");
    testUser.setCoins(1);

    // Act
    boolean result = transactionRepository.enoughCoins(testUser.getUsername());

    // Assert
    assertFalse(result);
  }
}
