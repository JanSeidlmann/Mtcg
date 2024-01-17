package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TradingController {
    private final TradingService tradingService;

    public TradingController() {
        this.tradingService = new TradingService();
    }

    public Response getTrades(Request request) {
        try {
            String trades = tradingService.getTrades();

            if(trades.isEmpty()){
                Response response = new Response();
                response.setStatus(HttpStatus.NO_CONTENT);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("The request was fine, but there are no trading deals available");
            }

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("There are trading deals available, the response contains these" + trades);

            return response;

        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public Response createTrade(Request request) {
        try {
            return tradingService.createTrade(request);
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public Response tradeCards(String tradeId,String buyerUsername, Request request) {
        try {
            tradingService.tradeCards(tradeId, buyerUsername, request);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Trading deal successfully executed.");

            return response;
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public Response deleteTrade(String tradeId, String username, Request request) {
        try {
            tradingService.deleteTrade(tradeId, username, request);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Trading deal successfully deleted");

            return response;
        } catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
