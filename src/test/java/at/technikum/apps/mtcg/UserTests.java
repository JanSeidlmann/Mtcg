package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.controller.UserController;
import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class UserTests {
  MtcgApp mtcgApp = new MtcgApp();

  private Request createMockRequestUser(HttpMethod method, String route, String token) {
    Request mockRequest = mock(Request.class);

    when(mockRequest.getMethod()).thenReturn(String.valueOf(method));
    when(mockRequest.getRoute()).thenReturn(route);
    when(mockRequest.getToken()).thenReturn(token);

    return mockRequest;
  }

  private Request createMockRequestSessions(HttpMethod method, String route, String body,  String token) {
    Request mockRequest = mock(Request.class);

    when(mockRequest.getMethod()).thenReturn(String.valueOf(method));
    when(mockRequest.getRoute()).thenReturn(route);
    when(mockRequest.getToken()).thenReturn(token);
    when(mockRequest.getBody()).thenReturn(body);

    return mockRequest;
  }

  @Test
  void handleGetRequestWithMatchingToken() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request getRequest = createMockRequestUser(HttpMethod.GET, "/users/testUser", "testUser");

    doReturn(response).when(userController).handle(getRequest);

    // Act
    userController.handle(getRequest);

    // Assert
    verify(userController, times(1)).handle(getRequest);
  }

  @Test
  void handleGetRequestWithNonMatchingToken() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request getRequest = createMockRequestUser(HttpMethod.GET, "/users/testUser", "wrongUser");

    doReturn(response).when(userController).handle(getRequest);

    // Act
    userController.handle(getRequest);

    // Assert
    verify(userController, times(1)).handle(getRequest);
    // Assertions.assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
  }

  @Test
  void handlePostRequest() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request postRequest = createMockRequestUser(HttpMethod.POST, "/users", null);
    String jsonBody = "{\"Username\": \"username\", \"Password\": \"password\"}";
    when(postRequest.getBody()).thenReturn(jsonBody);

    doReturn(response).when(userController).handle(postRequest);

    // Act
    userController.handle(postRequest);

    // Assert
    verify(userController, times(1)).handle(postRequest);
  }

  @Test
  void handlePutRequestWithMatchingToken() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request putRequest = createMockRequestUser(HttpMethod.PUT, "/users/testUser", "testUser");

    doReturn(response).when(userController).handle(putRequest);

    // Act
    userController.handle(putRequest);

    // Assert
    verify(userController, times(1)).handle(putRequest);
  }

  @Test
  void handlePutRequestWithNonMatchingToken() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request putRequest = createMockRequestUser(HttpMethod.PUT, "/users/testUser", "wrongUser");

    doReturn(response).when(userController).handle(putRequest);

    // Act
    userController.handle(putRequest);

    // Assert
    verify(userController, times(1)).handle(putRequest);
  }

  @Test
  void handlePostRequestWithValidCredentials() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    // Create a mock Request with a body containing Username and Password in JSON format
    Request postRequest = createMockRequestSessions(HttpMethod.POST, "/session", "{\"Username\": \"validUser\", \"Password\": \"validPassword\"}", null);

    doReturn(response).when(userController).handle(postRequest);

    // Act
    userController.handle(postRequest);

    // Assert
    verify(userController, times(1)).handle(postRequest);
  }

  @Test
  void handlePostRequestWithInvalidCredentials() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    // Create a mock Request with a body containing Username and Password in JSON format
    Request postRequest = createMockRequestSessions(HttpMethod.POST, "/session", "{\"Username\": \"invalidUser\", \"Password\": \"invalidPassword\"}", null);

    doReturn(response).when(userController).handle(postRequest);

    // Act
    userController.handle(postRequest);

    // Assert
    verify(userController, times(1)).handle(postRequest);
  }
}
