package at.technikum.apps.mtcg.repository;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
  UUID USER_ID = UUID.randomUUID();
  String USERNAME = "username";
  String PASSWORD = "password";
  UserRepository userRepository;


  @BeforeEach
  void setUp(){
    userRepository = new UserRepository();
    Utils.cleanDatabase();
  }

  @Test
  void saveUser() {
    //Arrange
    User user = new User();
    user.setId(USER_ID.toString());
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);

    //Act
    userRepository.saveUser(user);

    //assert
    var repositoryUser = userRepository.findUserByUsername(USERNAME).orElseThrow();
    assertEquals(repositoryUser.getId(), user.getId());
    assertEquals(repositoryUser.getUsername(), user.getUsername());
  }

//  @Test
//  void findUserByUsername() {
//  }
}