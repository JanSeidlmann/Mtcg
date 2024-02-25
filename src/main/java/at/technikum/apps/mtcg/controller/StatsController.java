package at.technikum.apps.mtcg.controller;
import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class StatsController implements Controller {
  private final StatsService statsService;
  private final PackageService packageService;

  public StatsController() {
    this.statsService = new StatsService();
    this.packageService = new PackageService();
  }

  @Override
  public boolean supports(String route) {
    return route.equals("/stats");
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
    if (request.getRoute().equals("/stats") && (request.getMethod().equals("GET"))) {
      return getStats(request);
    }
    return status(HttpStatus.BAD_REQUEST);
  }

  private Response getStats(Request request) {
    try {
      String username = packageService.extractUsernameFromToken(request.getToken());
      Stats stats = statsService.getStats(username);

      Response response = new Response();
      response.setStatus(HttpStatus.OK);
      response.setContentType(HttpContentType.APPLICATION_JSON);
      response.setBody(stats.toString());
      return response;
    } catch (Exception e){
      Response response = new Response();
      response.setStatus(HttpStatus.CONFLICT);
      response.setContentType(HttpContentType.APPLICATION_JSON);
      response.setBody("Failed to retrieve data.");
      return response;
    }
  }
}
