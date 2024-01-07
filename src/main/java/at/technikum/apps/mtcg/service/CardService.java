package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class CardService {

    private final CardRepository cardRepository;

    public CardService() {
        this.cardRepository = new CardRepository();
    }

    public Response getCards(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = extractUsernameFromToken(token);

            List<Card> cards = cardRepository.getCards(username);

            String cardJson = objectMapper.writeValueAsString(cards);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(cardJson);
            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

//    private Response createErrorResponse(String message) {
//        Response response = new Response();
//        response.setStatus(HttpStatus.CONFLICT);
//        response.setContentType(HttpContentType.APPLICATION_JSON);
//        response.setBody(message);
//        return response;
//    }

    public String extractUsernameFromToken(String token) {
        if (token != null && token.endsWith("-mtcgToken")) {
            int tokenIndex = token.indexOf("-mtcgToken");
            return token.substring(0, tokenIndex);
        } else {
            throw new RuntimeException("Invalid token.");
        }
    }
}
