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
    private final PackageService packageService;

    public CardService(CardRepository cardRepository, PackageService packageService) {
        this.cardRepository = cardRepository;
        this.packageService = packageService;
    }

    public Response getCards(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = packageService.extractUsernameFromToken(token);

            List<Card> cards = cardRepository.getCards(username);

            String cardJson = objectMapper.writeValueAsString(cards);

            if (!cards.isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The user has cards, the response contains these" + cardJson);
                return response;
            } else {
                Response response = new Response();
                response.setStatus(HttpStatus.NO_CONTENT);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The request was fine, but the user doesn't have any cards");
                return response;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
