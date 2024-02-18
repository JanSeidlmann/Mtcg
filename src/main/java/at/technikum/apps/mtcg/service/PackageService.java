package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.Repository;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

public class PackageService {
    private final Repository repository;

    public PackageService(){
        this.repository = new PackageRepository();
    }

    public Response createPackage(Request request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Card> cards = Arrays.asList(objectMapper.readValue(request.getBody(), Card[].class));

            String token = request.getToken();
            String username = extractUsernameFromToken(token);

            if (!username.equals("admin")){
                Response response = new Response();
                response.setStatus(HttpStatus.CREATED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Provided user is not \"admin\"");
                return response;
            } else {
                repository.addCards(cards);

                Response response = new Response();
                response.setStatus(HttpStatus.CREATED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody("Package and cards successfully created");
                return response;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    public String extractUsernameFromToken(String token) {
        String tokenPrefix = "Bearer ";
        int usernameStartIndex = token.indexOf(tokenPrefix);

        if (usernameStartIndex != -1) {
            usernameStartIndex += tokenPrefix.length();
            int tokenIndex = token.indexOf("-mtcgToken", usernameStartIndex);

            if (tokenIndex != -1) {
                return token.substring(usernameStartIndex, tokenIndex);
            }
        }

        throw new IllegalArgumentException("Invalid token format");
    }

}
