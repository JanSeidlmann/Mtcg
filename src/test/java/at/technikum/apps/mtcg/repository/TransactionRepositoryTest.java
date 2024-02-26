package at.technikum.apps.mtcg.repository;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TransactionRepositoryTest {
  private TransactionRepository transactionRepository;
  private PackageRepository packageRepository;
  private UserRepository userRepository;

  @BeforeEach
  void cleanDatabase(){
    this.transactionRepository = new TransactionRepository();
    this.packageRepository = new PackageRepository();
    this.userRepository = new UserRepository();
    Utils.cleanDatabase();
  }

  @Test
  void testDeletePackage() {
    // Arrange
    List<Card> cards = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Card card = new Card();
      card.setName("Fire");
      card.setId("card" + i);
      cards.add(card);
    }

    packageRepository.addCards(cards);
    transactionRepository.deletePackage();

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      transactionRepository.deletePackage();
    });

    // Assert
    assertEquals("No packages found to delete", exception.getMessage());
  }

  @Test
  void enoughCoinsValid(){
    // Arrange
    User testUser = new User();
    testUser.setId("123");
    testUser.setUsername("testuser");
    testUser.setPassword("password");
    userRepository.saveUser(testUser);

    // Act
    boolean result = transactionRepository.enoughCoins(testUser.getUsername());

    // Assert
    assertTrue(result);
  }

  @Test
  void enoughCoinsInvalid(){
    // Arrange
    User testUser = new User();
    testUser.setId("123");
    testUser.setUsername("testuser");
    testUser.setPassword("password");
    userRepository.saveUser(testUser);
    boolean result;

    for(int i = 0; i < 4; i++){
      transactionRepository.deductCoins(testUser.getUsername());
    }

    // Act
    result = transactionRepository.enoughCoins(testUser.getUsername());

    // Assert
    assertFalse(result);
  }

  @Test
  void enoughCoinsInvalidException(){
    // Arrange
    User testUser = new User();
    testUser.setId("123");
    testUser.setUsername("testuser");
    testUser.setPassword("password");
    userRepository.saveUser(testUser);

    for(int i = 0; i < 4; i++){
      transactionRepository.deductCoins(testUser.getUsername());
    }

    // Act
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      transactionRepository.deductCoins(testUser.getUsername());
    });

    // Assert
    assertEquals("Not enough coins!", exception.getMessage());
  }
}
