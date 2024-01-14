package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TradingController {
    private final TradingService tradingService;

    public TradingController() {
        this.tradingService = new TradingService();
    }

//    public Response getTrades(Request request) {
//        return tradingService.getTraids(request);
//    }

    public Response createTrade(Request request) {
        return tradingService.createTrade(request);
    }
}
