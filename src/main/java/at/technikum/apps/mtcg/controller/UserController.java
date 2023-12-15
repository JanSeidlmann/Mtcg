package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class UserController implements Controller {

    private final UserService userService;
    private final Repository repository;

    public UserController() {
        this.userService = new UserService();
        this.repository = new DatabaseRepository();
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
        String[] routeParts = request.getRoute().split("/");

        if (request.getRoute().equals("/users")) {
            switch (request.getMethod()) {
                case "POST":
                    return createUser(request);
                default: return status(HttpStatus.BAD_REQUEST); //Besser 405
            }
        } else if ( routeParts.length == 3) {
            String username = routeParts[2];

            switch (request.getMethod()) {
                case "GET":
                    return findUserByUsername(username, request);
                case "PUT":
                    return updateUserByUsername(username, request);
                default: return status(HttpStatus.BAD_REQUEST); //Besser 405
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    private Response findUserByUsername(String username, Request request) {
        Optional<User> userOptional = userService.findUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String userJson;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                userJson = objectMapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error processing JSON", e);
            }

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(userJson);

            return response;
        } else {
            return status(HttpStatus.NOT_FOUND);
        }
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
                return status(HttpStatus.CONFLICT); // 409 Conflict status code
            }
        }

    private Response updateUserByUsername(String username, Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser = null;

        try {
            updatedUser = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try {
            Optional<User> existingUserOptional = userService.findUserByUsername(username);

            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                userService.updateUserByUsername(username, request);
                String updatedUserJson = objectMapper.writeValueAsString(existingUser);

                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(updatedUserJson);

                return response;
            } else {
                return status(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
