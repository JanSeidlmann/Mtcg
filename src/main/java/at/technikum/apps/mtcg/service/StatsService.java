package at.technikum.apps.mtcg.service;
import at.technikum.apps.mtcg.entity.Stats;
import at.technikum.apps.mtcg.repository.StatsRepository;

public class StatsService {
  private final StatsRepository statsRepository;

  public StatsService() {
    this.statsRepository = new StatsRepository();
  }

  public Stats getStats(String username) {
    return statsRepository.getStats(username);
  }
}
