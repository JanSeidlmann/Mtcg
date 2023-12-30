package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
import at.technikum.apps.mtcg.repository.DatabaseRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PackageController {

    private final Repository repository;

    public PackageController(){
        this.repository = new DatabaseRepository();
    }

    public Response createPackage(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Card> cards = Arrays.asList(objectMapper.readValue(request.getBody(), Card[].class));

            Package pack = new Package();
            pack.addCards(cards);

            pack = repository.savePackage(pack);

            String packJson = objectMapper.writeValueAsString(pack);

            Response response = new Response();
            response.setStatus(HttpStatus.CREATED);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(packJson);
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
