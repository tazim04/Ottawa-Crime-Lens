package com.crimelens.crimelens_pipeline.job;

import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrimeIngestionJob {

    private final CrimeIngestionService ingestionService;
    private final ReentrantLock lock = new ReentrantLock();

    @Scheduled(cron = "${pipeline.cron}")
    public void run() {
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
