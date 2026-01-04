package com.crimelens.crimelens_pipeline.runner;

import com.crimelens.crimelens_pipeline.dto.IngestionResult;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.service.notification.PipelineNotifier;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!integration")
@RequiredArgsConstructor
public class CrimeIngestionPipelineRunner implements CommandLineRunner {

  private final CrimeIngestionService service;
  private final JdbcTemplate jdbcTemplate;
  private final PipelineNotifier notifier;
  private final String runId = Instant.now().toString();

  private void refreshAggregations() {
    log.info("Refreshing materialized view: crime_stats_grid...");
    jdbcTemplate.execute("REFRESH MATERIALIZED VIEW crime_stats_grid");
    log.info("Materialized view crime_stats_grid refreshed successfully.");
  }

  @Override
  public void run(String... args) {
    try {
      Instant start = Instant.now();
      log.info("Starting CrimeLens data ingestion...");
      notifier.running(runId);
      IngestionResult results = service.run();
      refreshAggregations();

      log.info("Successfully finished CrimeLens data ingestion!");
      Instant end = Instant.now();
      Duration timeElapsed = Duration.between(start, end);
      notifier.success(
          runId, timeElapsed, results.fetched(), results.inserted(), results.duplicates());
    } catch (Exception e) {
      log.error("CrimeLens ingestion job failed!", e);
      notifier.failure(runId, e);
      throw e;
    }
  }
}
