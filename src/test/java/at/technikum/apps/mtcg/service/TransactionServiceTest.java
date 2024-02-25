package at.technikum.apps.mtcg.service;
import at.technikum.apps.mtcg.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TransactionServiceTest {
  private TransactionRepository transactionRepositoryMock;
  private TransactionService transactionService;

  @BeforeEach
  void setUp() {
    this.transactionRepositoryMock = mock(TransactionRepository.class);
    this.transactionService = new TransactionService(transactionRepositoryMock);
  }

  @Test
  void testAcquirePackages() {
    // Arrange
    String username = "testuser";

    // Act
    transactionService.acquirePackages(username);

    // Assert
    verify(transactionRepositoryMock).acquirePackages(username);
  }
}
