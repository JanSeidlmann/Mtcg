package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class PackageController implements Controller {

    private final PackageService packageService;

    public PackageController(){
        this.packageService = new PackageService();
    }
    @Override
    public boolean supports(String route) {
        return route.equals("/packages");
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
        if (request.getRoute().equals("/packages")) {
            switch (request.getMethod()) {
                case "POST":
                    return createPackage(request);
            }
        }
        return status(HttpStatus.BAD_REQUEST);
    }

    public Response createPackage(Request request) {
        try {
            return packageService.createPackage(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed creating package!");
        }
    }
}
