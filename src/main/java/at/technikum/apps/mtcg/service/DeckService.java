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
    private final PackageService packageService;

    public DeckService() {
        this.deckRepository = new DeckRepository();
        this.packageService = new PackageService();
    }

    public Response getDeck(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = packageService.extractUsernameFromToken(token);

            List<Card> deck = deckRepository.getDeck(username);

            String deckJson = objectMapper.writeValueAsString(deck);

            if(!deck.isEmpty()) {
                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The deck has cards, the response contains these" + deckJson);
                return response;
            } else {
                Response response = new Response();
                response.setStatus(HttpStatus.NO_CONTENT);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The request was fine, but the deck doesn't have any cards");
                return response;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public Response configureDeck(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String token = request.getToken();
            String username = packageService.extractUsernameFromToken(token);
            JsonNode cardIdsNode = objectMapper.readTree(request.getBody());
            List<Card> originalDeck = deckRepository.getDeck(username);
            String deckJson = objectMapper.writeValueAsString(originalDeck);
            List<String> cardIds = new ArrayList<>();
            for (JsonNode cardIdNode : cardIdsNode) {
                String cardId = cardIdNode.asText();
                if (!deckRepository.userOwnsCard(username, cardId)) {
                    Response response = new Response();
                    response.setStatus(HttpStatus.FORBIDDEN);
                    response.setContentType(HttpContentType.APPLICATION_JSON);
                    response.setBody("At least one of the provided cards does not belong to the user or is not available." + deckJson);
                    return response;
                }
                cardIds.add(cardId);
            }

            if (cardIds.size() < 4){
                Response response = new Response();
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The provided deck did not include the required amount of cards");
                return response;
            }

            // Fehlen noch Responses für 401 & 403

            deckRepository.configureDeck(username, cardIds);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("The deck has been successfully configured");
            return response;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
