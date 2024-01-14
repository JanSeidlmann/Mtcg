package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class TradingService {

    private final TradingRepository tradingRepository;
    private final PackageService packageService;

    public TradingService() {
        this.tradingRepository = new TradingRepository();
        this.packageService = new PackageService();
    }

//    public Response getTrades(Request request) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//             = objectMapper.readValue(request.getBody(), .class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//
//
//            String Json;
//            try {
//                Json = objectMapper.writeValueAsString();
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException("Error processing JSON", e);
//            }
//
//            Response response = new Response();
//            response.setStatus(HttpStatus.CREATED);
//            response.setContentType(HttpContentType.APPLICATION_JSON);
//            response.setBody(Json);
//
//            return response;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error processing JSON", e);
//        }
//    }

    public Response createTrade(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String token = request.getToken();

            if (token == null || !token.endsWith("mtcgToken")) {
                return packageService.createErrorResponse("Invalid token.");
            }

            String username = packageService.extractUsernameFromToken(token);
            Trade trade = objectMapper.readValue(request.getBody(), Trade.class);
            String card_id = trade.getCard_id();

            List<String> userCards = tradingRepository.getCards();
            boolean cardBelongsToUser = userCards.contains(card_id);

            if (!cardBelongsToUser) {
                return packageService.createErrorResponse("The card does not belong to the user.");
            }

            List<String> userDeck = tradingRepository.getDeck(username);
            boolean cardInUserDeck = userDeck.contains(card_id);

            if (cardInUserDeck) {
                return packageService.createErrorResponse("The card is already in the user's deck.");
            }

            tradingRepository.createTrade(trade.getTrade_id(), trade.getCard_id(), trade.getType(), trade.getDamage());

            String jsonResponse = objectMapper.writeValueAsString(trade);

            Response response = new Response();
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(jsonResponse);

            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
