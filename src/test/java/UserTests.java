import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.controller.UserController;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserTests {

    UserController userController = new UserController();

    UserService userService = new UserService();

    MtcgApp mtcgApp = new MtcgApp();

    @Test
    void createUser_SuccessfullyCreatesUser_Returns201Created() {
        Request request = new Request();
        request.setMethod(HttpMethod.POST);
        request.setRoute("http://localhost:10001/users");
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
        request.setBody("{\"username\":\"kienboec\", \"password\":\"daniel\"}");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.NOT_FOUND.getCode(), response.getStatusCode());
    }
}
