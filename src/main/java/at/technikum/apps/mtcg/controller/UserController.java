package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.Exception.DuplicateUserException;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.DatabaseRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.TransactionService;
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
    private final PackageController packageController;
    private final PackageService packageService;
    private final TransactionController transactionController;
    private final CardController cardController;
    private final DeckController deckController;
    private final BattleController battleController;
    private final TradingController tradingController;

    public UserController() {
        this.userService = new UserService();
        this.packageController = new PackageController();
        this.packageService = new PackageService();
        this.transactionController = new TransactionController();
        this.cardController = new CardController();
        this.deckController = new DeckController();
        this.battleController = new BattleController();
        this.tradingController = new TradingController();
    }

    @Override
    public boolean supports(String route) {
        String[] routeParts = route.split("/");

        return route.startsWith("/users") || (routeParts.length == 3 && "tradings".equals(routeParts[1])) || route.equals("/tradings") || route.equals("/battle") || route.equals("/cards") || route.equals("/sessions") || route.equals("/packages") || route.equals("/transactions/packages") || route.equals("/deck");
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
        } else if (routeParts.length == 3 && !request.getRoute().equals("/transactions/packages")) {
            if ("users".equals(routeParts[1])){
                String username = routeParts[2];
                switch (request.getMethod()) {
                    case "GET":
                        return findUserByUsername(username, request);
                    case "PUT":
                        return updateUserByUsername(username, request);
                    default:
                        return status(HttpStatus.BAD_REQUEST); // Besser 405
                }
            } else if ("tradings".equals(routeParts[1])){
                String trade_id = routeParts[2];
                String token = request.getToken();
                String username = packageService.extractUsernameFromToken(token);
                switch (request.getMethod()){
                    case "POST":
                        return tradingController.tradeCards(trade_id, username, request);
                    case "DELETE":
                        return tradingController.deleteTrade(trade_id, username, request);
                }
            }
        } else if (request.getRoute().equals("/packages")) {
            switch (request.getMethod()) {
                case "POST":
                    return packageController.createPackage(request);
            }
        } else if (request.getRoute().equals("/transactions/packages")) {
            switch (request.getMethod()) {
                case "POST":
                    return transactionController.acquirePackages(request);
            }
        } else if (request.getRoute().equals("/cards")) {
            switch (request.getMethod()) {
                case "GET":
                    return cardController.getCards(request);
            }
        } else if (request.getRoute().equals("/deck")) {
            switch (request.getMethod()) {
                case "GET":
                    return deckController.getDeck(request);
                case "PUT":
                    return deckController.configureDeck(request);
            }
        } else if (request.getRoute().equals("/battle")){
            switch (request.getMethod()){
                case "POST":
                    return battleController.startBattle(request);
            }
        } else if (request.getRoute().equals("/tradings")){
            switch (request.getMethod()){
                case "GET":
                    return tradingController.getTrades(request);
                case "POST":
                    return tradingController.createTrade(request);
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

    private Response createUser(Request request) {

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

    private Response loginUser(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getBody()); // Geht anders mit objectmapper damit gleich
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();

            Optional<User> existingUserOptional = userService.login(username, password);

            if (existingUserOptional.isPresent()) {
                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Successfully logged in!");

                return response;
            } else {
                return status(HttpStatus.UNAUTHORIZED);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
