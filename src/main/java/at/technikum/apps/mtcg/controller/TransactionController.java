package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.TransactionRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TransactionController implements Controller {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final PackageService packageService;

    public TransactionController() {
        this.transactionService = new TransactionService();
        this.packageService = new PackageService();
        this.transactionRepository = new TransactionRepository();
    }

    @Override
    public boolean supports(String route) {
        return route.startsWith("/transactions");
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
        if (request.getRoute().startsWith("/transactions") && (request.getMethod().equals("POST"))) {
            return acquirePackages(request);
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    public Response acquirePackages(Request request) {
        try {

            String username = packageService.extractUsernameFromToken(request.getToken());

            if (transactionRepository.enoughCoins(username)){
                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Not enough money for buying a card package");
                return response;
            }

            transactionService.acquirePackages(username);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("A package has been successfully bought");
            return response;

        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("No card package available for buying");
            return response;
        }
    }
}
