package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.HttpStatus;

public class DeckController {
    private final DeckService deckService;

    public DeckController() {
        this.deckService = new DeckService();
    }

    public Response getDeck(Request request) {
        try {
            return deckService.getDeck(request);
        } catch (Exception e) {
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
