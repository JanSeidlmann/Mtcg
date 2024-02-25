package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.repository.DeckRepository;
import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TradingService {

    private final TradingRepository tradingRepository;
    private final PackageService packageService;
    private final DeckRepository deckRepository;

    public TradingService() {
        this.tradingRepository = new TradingRepository();
        this.packageService = new PackageService();
        this.deckRepository = new DeckRepository();
    }

    public void tradeCards(String tradeId, String buyerUsername, Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode traderCardId = objectMapper.readTree(request.getBody());

            List<Card> ownCardId = deckRepository.getDeck(buyerUsername);

            if(ownCardId.toString().contains(traderCardId.toString())) {
                return;
            }

            tradingRepository.tradeCards(tradeId, buyerUsername, traderCardId.asText());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public String getTrades() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            List<Trade> trades = tradingRepository.getTrades();

            String tradeJson = objectMapper.writeValueAsString(trades);

            return tradeJson;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public Response createTrade(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String token = request.getToken();

            if (token == null || !token.endsWith("mtcgToken")) {
                Response response = new Response();
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Access token is missing or invalid");
                return response;
            }

            String username = packageService.extractUsernameFromToken(token);
            JsonNode trade = objectMapper.readTree(request.getBody());
            String tradeId = trade.get("Id").asText();
            String cardId = trade.get("CardToTrade").asText();
            String type = trade.get("Type").asText();
            int damage = trade.get("MinimumDamage").asInt();

            List<Card> ownCardId = deckRepository.getDeck(username);

            if(ownCardId.toString().contains(cardId)) {
                Response response = new Response();
                response.setStatus(HttpStatus.FORBIDDEN);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The specified card is already in the user's deck");
                return response;
            }

            List<String> userCards = tradingRepository.getCards(username);
            boolean cardBelongsToUser = userCards.contains(cardId);
            List<String> userDeck = tradingRepository.getDeck(username);
            boolean cardInUserDeck = userDeck.contains(cardId);

            if (!cardBelongsToUser || cardInUserDeck) {
                Response response = new Response();
                response.setStatus(HttpStatus.FORBIDDEN);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The deal contains a card that is not owned by the user or locked in the deck");
                return response;
            }

            tradingRepository.createTrade(request, tradeId, cardId, type, damage);

            Response response = new Response();
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Trading deal successfully created");
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public void deleteTrade(String tradeId, String username, Request request) {
        String token = request.getToken();
        if (token == null || !token.endsWith("mtcgToken")) {
            throw new RuntimeException("Invalid Token!");
        }

        username = packageService.extractUsernameFromToken(token);

        tradingRepository.deleteTrade(tradeId, username);
    }
}
