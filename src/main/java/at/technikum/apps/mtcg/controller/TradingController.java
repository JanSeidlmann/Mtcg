package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TradingController implements Controller {
    private final TradingService tradingService;
    private final TradingRepository tradingRepository;
    private final PackageService packageService;

    public TradingController() {
        this.tradingService = new TradingService();
        this.tradingRepository = new TradingRepository();
        this.packageService = new PackageService();
    }

    @Override
    public boolean supports(String route) {
        String[] routeParts = route.split("/");
        return (routeParts.length == 3 && "tradings".equals(routeParts[1])) || route.equals("/tradings");
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
        String[] routeParts = request.getRoute().split("/");

        if ("tradings".equals(routeParts[1]) && routeParts.length > 2){
            String trade_id = routeParts[2];
            String token = request.getToken();
            String username = packageService.extractUsernameFromToken(token);
            switch (request.getMethod()){
                case "POST":
                    return tradeCards(trade_id, username, request);
                case "DELETE":
                    return deleteTrade(trade_id, username, request);
                default:
                    return status(HttpStatus.BAD_REQUEST); // Besser 405
            }
        } else if (request.getRoute().equals("/tradings")){
            switch (request.getMethod()){
                case "GET":
                    return getTrades();
                case "POST":
                    return createTrade(request);
                default:
                    return status(HttpStatus.BAD_REQUEST); // Besser 405
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    public Response getTrades() {
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
            String sellerUsername = tradingRepository.selectSellerUsername(tradeId);

            if (sellerUsername.equals(buyerUsername)){
                Response response = new Response();
                response.setStatus(HttpStatus.CONFLICT);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("You can't trade with yourself");
                return response;
            }

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
