package com.crimelens.crimelens_pipeline.job;

import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "pipeline.cron.enabled", havingValue = "true")
@RequiredArgsConstructor
public class CrimeIngestionJob {

  private final CrimeIngestionService ingestionService;
  private final ReentrantLock lock = new ReentrantLock();

  @Scheduled(cron = "${pipeline.cron}")
  public void run() {
    log.info("Starting up scheduled pipeline ingestion job!!!!!!!!!!!!!!!!");
    // Try to acquire the lock to ensure no other instance of the pipeline is running
    if (!lock.tryLock()) {
      log.warn("Previous ingestion still running - skipping this run!");
      return;
    }
    try {
      log.info("Starting CrimeLens ingestion job");
      ingestionService.ingest();
      log.info("Finished CrimeLens ingestion job");
    } catch (Exception e) {
      log.error("Ingestion job failed", e);
    } finally {
      // release the lock
      lock.unlock();
    }
  }
}
