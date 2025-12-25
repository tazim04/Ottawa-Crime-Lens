package com.crimelens.crimelens_pipeline.runner;

import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevStartupIngestionRunner implements ApplicationRunner {

  private final CrimeIngestionService service;

  @Override
  public void run(ApplicationArguments args) {
    log.info("Starting up pipeline in DEV environment!!!!!!!!!!!!!");
    service.ingest();
  }
}
