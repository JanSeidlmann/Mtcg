package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class CardController {

    private final CardService cardService;

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
