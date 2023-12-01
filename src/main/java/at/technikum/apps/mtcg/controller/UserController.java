package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class UserController implements Controller {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @Override
    public boolean supports(String route) {
        return route.startsWith("/users");
    }

    @Override
    public Response status(HttpStatus httpStatus) {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("{ \"error\": \"" + httpStatus.getMessage() + "\"}");

        return response;
    }

    @Override
    public Response handle(Request request) {

        if (request.getRoute().equals("/users")) {
            switch (request.getMethod()) {
                case "GET":
                    return findAllUsers(request);
                case "POST":
                    return createUser(request);
                default: return status(HttpStatus.BAD_REQUEST); //Besser 405
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    private Response findAllUsers(Request request) {
        List<User> tasks = userService.findAllUsers();

        ObjectMapper objectMapper = new ObjectMapper();
        String tasksJson = null;
        try {
            tasksJson = objectMapper.writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(tasksJson);

        return response;
    }

    private Response createUser(Request request){

            ObjectMapper objectMapper = new ObjectMapper();
            User user;
            try {
                user = objectMapper.readValue(request.getBody(), User.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            try {
                user = userService.save(user);

                String userJson;
                try {
                    userJson = objectMapper.writeValueAsString(user);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error processing JSON", e);
                }

                Response response = new Response();
                response.setStatus(HttpStatus.CREATED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(userJson);

                return response;
            } catch (DuplicateUserException e) {
                // Benutzer mit gleichem Benutzernamen existiert bereits
                return status(HttpStatus.CONFLICT); // 409 Conflict status code
            }
        }
}
