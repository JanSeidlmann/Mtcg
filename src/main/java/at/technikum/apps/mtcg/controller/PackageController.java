package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class PackageController {

    private final PackageService packageService;

    public PackageController(){
        this.packageService = new PackageService();
    }

    public Response createPackage(Request request) {
        try {
            return packageService.createPackage(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed creating package!");
        }
    }
}
