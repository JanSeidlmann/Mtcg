package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Package;
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

            if (token == null || !token.endsWith("mtcgToken")) {
                return createErrorResponse("Invalid token.");
            }

            String username = extractUsernameFromToken(token);

            if ("admin".equals(username)) {
                String packJson = objectMapper.writeValueAsString(cards);

                repository.addCards(cards);

                Response response = new Response();
                response.setStatus(HttpStatus.CREATED);
                response.setContentType(HttpContentType.APPLICATION_JSON);
                response.setBody(packJson);
                return response;
            } else {
                return createErrorResponse("Permission denied.");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    private Response createErrorResponse(String message) {
        Response response = new Response();
        response.setStatus(HttpStatus.CONFLICT);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody(message);
        return response;
    }

    public String extractUsernameFromToken(String token) {
        int tokenIndex = token.indexOf("-mtcgToken");
        return token.substring(0, tokenIndex);
    }
}
