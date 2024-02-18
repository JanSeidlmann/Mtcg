package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class CardController implements Controller {

    private final CardService cardService;

    @Override
    public boolean supports(String route) {
        return route.equals("/cards");
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
        if (request.getRoute().equals("/cards")) {
            switch (request.getMethod()) {
                case "GET":
                    return getCards(request);
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    public CardController() {
        this.cardService = new CardService();
    }

    public Response getCards(Request request) {
        try {
            return cardService.getCards(request);
        } catch (Exception e){
            Response response = new Response();
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Access token is missing or invalid");
            return response;
        }
    }
}
