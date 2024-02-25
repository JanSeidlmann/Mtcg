package at.technikum.apps.mtcg.service;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.utils.Utils;
import at.technikum.server.http.Request;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserServiceTest {
private UserRepository repositoryMock;
private UserService userServiceTest;


  @BeforeEach
  void setUp(){
    this.repositoryMock = mock(UserRepository.class);
    this.userServiceTest = new UserService(repositoryMock);
  }

  @AfterEach
  void tearDown(){
    Utils.cleanDatabase();
  }

  @Test
  void findUserByUsername() {
    // Arrange
    String username = "testuser";

    // Act
    userServiceTest.findUserByUsername(username);

    // Assert
    verify(repositoryMock).findUserByUsername(username);
  }

  @Test
  void updateUserByUsername() {
    // Arrange
    String username = "testuser";
    Request request = new Request();

    // Act
    userServiceTest.updateUserByUsername(username, request);

    // Assert
    verify(repositoryMock).updateUserByUsername(username, request);
  }

  @Test
  void testLoginWithCorrectCredentials() {
    // Arrange
    String username = "testuser";
    String password = "testpassword";
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    when(repositoryMock.findUserByUsername(username)).thenReturn(Optional.of(user));

    // Act
    Optional<User> loggedInUser = userServiceTest.login(username, password);

    // Assert
    assertTrue(loggedInUser.isPresent());
    assertEquals(user, loggedInUser.get());
  }

  @Test
  void testLoginWithIncorrectUsername() {
    // Arrange
    String username = "testuser";
    String password = "testpassword";
    when(repositoryMock.findUserByUsername(username)).thenReturn(Optional.empty());

    // Act
    Optional<User> loggedInUser = userServiceTest.login(username, password);

    // Assert
    assertTrue(loggedInUser.isEmpty());
  }

  @Test
  void testLoginWithIncorrectPassword() {
    // Arrange
    String username = "testuser";
    String password = "wrongpassword";
    when(repositoryMock.findUserByUsername(username)).thenReturn(Optional.empty());

    // Act
    Optional<User> loggedInUser = userServiceTest.login(username, password);

    // Assert
    assertTrue(loggedInUser.isEmpty());
  }

  @Test
  void testSaveUser() {
    // Arrange
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("testpassword");

    // Act
    userServiceTest.save(user);

    // Assert
    verify(repositoryMock).saveUser(user);
  }
}