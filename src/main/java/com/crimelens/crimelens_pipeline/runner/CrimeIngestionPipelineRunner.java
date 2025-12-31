package com.crimelens.crimelens_pipeline.runner;

import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class CrimeIngestionPipelineRunner implements CommandLineRunner {

  private final CrimeIngestionService service;

  @Override
  public void run(String... args) {
    log.info("Starting CrimeLens data ingestion...");
    service.run();
    log.info("Successfully finished CrimeLens data ingestion!");
  }
}
