package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class TradingService {

    private final TradingRepository tradingRepository;
    private final PackageService packageService;

    public TradingService() {
        this.tradingRepository = new TradingRepository();
        this.packageService = new PackageService();
    }

    public void tradeCards(String tradeId, String buyerUsername, Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(request.getBody());
            JsonNode cardIdsNode = rootNode.get("card_id");

            tradingRepository.tradeCards(tradeId, buyerUsername, cardIdsNode.asText());

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


    public String createTrade(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String token = request.getToken();

            if (token == null || !token.endsWith("mtcgToken")) {
                return "Invalid Token!";
            }

            String username = packageService.extractUsernameFromToken(token);
            Trade trade = objectMapper.readValue(request.getBody(), Trade.class);
            String card_id = trade.getCard_id();

            List<String> userCards = tradingRepository.getCards();
            boolean cardBelongsToUser = userCards.contains(card_id);

            if (!cardBelongsToUser) {
                return "The card does not belong to the user!";
            }

            List<String> userDeck = tradingRepository.getDeck(username);
            boolean cardInUserDeck = userDeck.contains(card_id);

            if (cardInUserDeck) {
                return "The card is already in the user's deck!";
            }

            tradingRepository.createTrade(request, trade.getTrade_id(), trade.getCard_id(), trade.getType(), trade.getDamage());

            return username;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
