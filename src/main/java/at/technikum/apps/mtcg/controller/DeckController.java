package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.HttpStatus;

public class DeckController implements Controller {
    private final DeckService deckService;

    public DeckController() {
        this.deckService = new DeckService();
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/deck");
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
        if (request.getRoute().equals("/deck")) {
            switch (request.getMethod()) {
                case "GET":
                    return getDeck(request);
                case "PUT":
                    return configureDeck(request);
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    public Response getDeck(Request request) {
        try {
            return deckService.getDeck(request);
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("Error getting user deck");
        }
    }

    public Response configureDeck(Request request) {
        try {
            return deckService.configureDeck(request);
        } catch (Exception e) {
            return createErrorResponse("Error configuring user deck");
        }
    }

    private Response createErrorResponse(String message) {
        Response response = new Response();
        response.setStatus(HttpStatus.CONFLICT);
        response.setBody(message);
        return response;
    }
}
