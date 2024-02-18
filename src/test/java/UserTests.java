import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.controller.UserController;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTests {
  MtcgApp mtcgApp = new MtcgApp();
//  @Test
//  void createUser() {
//    Request request = new Request();
//    request.setMethod(HttpMethod.POST);
//    request.setRoute("http://localhost:10001/users");
//    request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
//    request.setBody("{\"username\":\"kienboec\", \"password\":\"daniel\"}");
//
//    Response response = mtcgApp.handle(request);
//
//    assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
//  }
}
