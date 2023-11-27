package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

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
        response.setBody("{ \"error\": \""+ httpStatus.getMessage() + "\"}");

        return response;
    }

    @Override
    public Response handle(Request request) {

        if (request.getRoute().equals("/users")){
            return createUser(request);
        }

        return status(HttpStatus.BAD_REQUEST); //Besser 405
    }

    private Response createUser(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        user = userService.save(user);

        String taskJson = null;
        try {
            taskJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.CREATED);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(taskJson);

        return response;
    }


}
