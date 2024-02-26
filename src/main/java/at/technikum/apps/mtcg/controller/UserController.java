package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public class UserController implements Controller {

    private final UserService userService;
    private final PackageService packageService;

    public UserController() {
        this.userService = new UserService();
        this.packageService = new PackageService();
    }

    public UserController(UserService userService, PackageService packageService){
        this.userService = userService;
        this.packageService = packageService;
    }

    @Override
    public boolean supports(String route) {
        return route.startsWith("/users") || route.equals("/sessions");
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
                default:
                    return status(HttpStatus.BAD_REQUEST); // Besser 405
            }
        } else if (request.getRoute().equals("/sessions")) {
            switch (request.getMethod()) {
                case "POST":
                    return loginUser(request);
                default:
                    return status(HttpStatus.BAD_REQUEST); // Besser 405
            }
        } else if (routeParts.length == 3) {
                String username = routeParts[2];
                switch (request.getMethod()) {
                    case "GET":
                        return findUserByUsername(username, request);
                    case "PUT":
                        return updateUserByUsername(username, request);
                    default:
                        return status(HttpStatus.BAD_REQUEST); // Besser 405
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    private Response findUserByUsername(String username, Request request) {
        Optional<User> userOptional = userService.findUserByUsername(username);
        String token = request.getToken();
        String tokenUsername = packageService.extractUsernameFromToken(token);

        if(!username.equals(tokenUsername)) {
            Response response = new Response();
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Access token is missing or invalid");

            return response;
        } else if (userOptional.isPresent() || username.equals("admin")) {
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Data successfully retrieved");

            return response;
        } else {
            Response response = new Response();
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User not found.");

            return response;
        }
    }

    public Response createUser(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User user;
        try {
            user = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            userService.save(user);

            Response response = new Response();
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User successfully created");

            return response;
        } catch (DuplicateUserException e) {
            Response response = new Response();
            response.setStatus(HttpStatus.CONFLICT);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("User with same username already registered");

            return response;
        }
    }

    private Response updateUserByUsername(String username, Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser;

        try {
            updatedUser = objectMapper.readValue(request.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Optional<User> existingUserOptional = userService.findUserByUsername(username);
        String token = request.getToken();
        String tokenUsername = packageService.extractUsernameFromToken(token);

        if(!username.equals(tokenUsername)) {
            Response response = new Response();
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Access token is missing or invalid");

            return response;
        }
        if (existingUserOptional.isPresent()) {
          User existingUser = existingUserOptional.get();
          existingUser.setUsername(updatedUser.getUsername());
          existingUser.setPassword(updatedUser.getPassword());
          userService.updateUserByUsername(username, request);

          Response response = new Response();
          response.setStatus(HttpStatus.OK);
          response.setContentType(HttpContentType.APPLICATION_JSON);
          response.setBody("User successfully updated");

          return response;
        } else {
          return status(HttpStatus.BAD_REQUEST);
        }
    }

    public Response loginUser(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody()); // Geht anders mit objectmapper damit gleich
            String username = jsonNode.get("Username").asText();
            String password = jsonNode.get("Password").asText();

            Optional<User> existingUserOptional = userService.login(username, password);

            if (existingUserOptional.isPresent()) {
                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("User login successful");

                return response;
            } else {
                Response response = new Response();
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Invalid username/password provided");

                return response;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
