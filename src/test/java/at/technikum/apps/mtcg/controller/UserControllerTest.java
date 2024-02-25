package at.technikum.apps.mtcg.controller;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
  UserService userServiceMock = mock(UserService.class);
  PackageService packageServiceMock = mock(PackageService.class);
  UserController userController = new UserController(userServiceMock, packageServiceMock);

  @Test
  void testCreateUser() {
    // Arrange
    Request requestMock = mock(Request.class);
    String requestBody = "{\"Username\": \"testuser\", \"Password\": \"testpassword\"}";
    when(requestMock.getBody()).thenReturn(requestBody);

    User expectedUser = new User();
    expectedUser.setUsername("testuser");
    expectedUser.setPassword("testpassword");

    Response expectedResponse = new Response();
    expectedResponse.setStatus(HttpStatus.CREATED);
    expectedResponse.setContentType(HttpContentType.APPLICATION_JSON);
    expectedResponse.setBody("User successfully created");

    when(userServiceMock.save(expectedUser)).thenReturn(expectedUser);


    // Act
    Response actualResponse = userController.createUser(requestMock);

    // Assert
    verify(userServiceMock).save(expectedUser);
    assertEquals(expectedResponse, actualResponse);
  }
}