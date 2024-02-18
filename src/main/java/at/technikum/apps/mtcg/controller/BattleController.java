package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.BattleRepository;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.ArrayList;
import java.util.List;

public class BattleController implements Controller {

    private final BattleService battleService;
    private final PackageService packageService;

    private final List<String> players = new ArrayList<>();
    private final int TIMEOUT = 10;
    private boolean battleStarted = false;

    public BattleController() {
        this.battleService = new BattleService();
        this.packageService = new PackageService();
    }

    @Override
    public boolean supports(String route) {
      return route.equals("/battles");
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
      if (request.getRoute().equals("/battles") && (request.getMethod().equals("POST"))) {
          return startBattle(request);
      }
      return status(HttpStatus.BAD_REQUEST);
    }

    public synchronized Response startBattle(Request request) {

            String token = request.getToken();
            String player = packageService.extractUsernameFromToken(token);
            players.add(player);
            String player1 = players.get(0);

            if (players.size() == 1) {
                System.out.println("Waiting for opponent...\n");

//                try {
//                    wait(TIMEOUT * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (!battleStarted) {
//                    players.remove(player1);
//
//                    Response response = new Response();
//                    response.setStatus(HttpStatus.OK);
//                    response.setBody("No opponents available");
//                    System.out.println("No opponents available");
//                    return response;
//                } else {
//                    battleStarted = false;
//                }
            }

            if (players.size() == 2) {
                System.out.println("Game started.\n");
                String player2 = players.get(1);
                battleStarted = true;
                String battleLog = battleService.startBattle(players.get(0), players.get(1));
                System.out.println("Battle finished.");

                notifyAll();

                players.remove(player1);
                players.remove(player2);

                Response response = new Response();
                response.setStatus(HttpStatus.OK);
                response.setBody(battleLog);
                return response;
            }

        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("1 Player in Lobby.\n");
        return response;
    }
}
