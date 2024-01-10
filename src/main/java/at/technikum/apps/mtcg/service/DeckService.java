package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.DeckRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeckService {
    private final DeckRepository deckRepository;
    private final CardService cardService;

    public DeckService() {
        this.deckRepository = new DeckRepository();
        this.cardService = new CardService();
    }

    public Response getDeck(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = cardService.extractUsernameFromToken(token);

            List<Card> deck = deckRepository.getDeck(username);

            String deckJson = objectMapper.writeValueAsString(deck);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(deckJson);
            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public Response configureDeck(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = cardService.extractUsernameFromToken(token);
            JsonNode rootNode = objectMapper.readTree(request.getBody());
            JsonNode cardIdsNode = rootNode.get("cardIds");
            List<String> cardIds = new ArrayList<>();
            for (JsonNode cardIdNode : cardIdsNode) {
                cardIds.add(cardIdNode.asText());
            }

            deckRepository.configureDeck(username, cardIds);

            String confJson = objectMapper.writeValueAsString(cardIds);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(confJson);
            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
