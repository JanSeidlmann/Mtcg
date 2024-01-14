package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.BattleLog;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleController {

    private final BattleService battleService;
    private final PackageService packageService;

    public BattleController() {
        this.battleService = new BattleService();
        this.packageService = new PackageService();
    }

    public Response startBattle(Request request) {
        try {
            // Extrahiere die Spielerinformationen aus der Anfrage
//            String player1 = request.getParameter("player1");
//            String player2 = request.getParameter("player2");

            // Starte den Kampf und erhalte den BattleLog
            // BattleLog battleLog = battleService.startBattle(player1, player2);

            // Erstelle eine JSON-Repräsentation des BattleLogs für die Antwort
            // String jsonResponse = convertBattleLogToJson(battleLog);

            // Erstelle die Antwort
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
           // response.setBody(jsonResponse);
            return response;

        } catch (Exception e) {
            // Behandele Ausnahmen und erstelle eine fehlerhafte Antwort
            Response response = new Response();
            response.setStatus(HttpStatus.CONFLICT);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody("Error starting battle: " + e.getMessage());
            return response;
        }
    }

    // Diese Methode kann erweitert werden, um BattleLog in JSON zu konvertieren
    private String convertBattleLogToJson(BattleLog battleLog) {
        // Implementiere die Logik zur Konvertierung von BattleLog in JSON
        // Je nach verwendeter JSON-Bibliothek kann dies variieren
        // Ein einfaches Beispiel könnte mit Jackson wie folgt aussehen:
        // ObjectMapper objectMapper = new ObjectMapper();
        // return objectMapper.writeValueAsString(battleLog);
        return "JSON representation of BattleLog";
    }
}
