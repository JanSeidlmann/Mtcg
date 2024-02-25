package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.controller.UserController;
import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


class UserTests {

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
  void handleGetRequest() {
    // Arrange
    UserController userController = spy(new UserController());
    Response response = mock(Response.class);

    Request getRequest = createMockRequestUser(HttpMethod.GET, "/users/testUser", "testUser");

    doReturn(response).when(userController).handle(getRequest);

    // Act
    userController.handle(getRequest);

    // Assert
    verify(userController).handle(getRequest);
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
    verify(userController).handle(postRequest);
  }

  @Test
  void handlePutRequest() {
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
}
