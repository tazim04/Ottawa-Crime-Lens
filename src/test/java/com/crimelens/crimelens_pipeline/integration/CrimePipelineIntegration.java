package com.crimelens.crimelens_pipeline.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("integration")
class CrimePipelineIntegrationTest {

  // PostGIS container (real spatial DB)
  @Container
  static PostgreSQLContainer<?> postgis =
      new PostgreSQLContainer<>("postgis/postgis:16-3.4")
          .withDatabaseName("crimelens")
          .withUsername("test")
          .withPassword("test");

  // Inject container DB props into Spring Boot
  @DynamicPropertySource
  static void overrideDatasource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgis::getJdbcUrl);
    registry.add("spring.datasource.username", postgis::getUsername);
    registry.add("spring.datasource.password", postgis::getPassword);
  }

  @Autowired private CrimeIngestionService ingestionService;

  @Autowired private CrimeRecordRepository repository;

  @Test
  void pipeline_fetches_and_persists_crime_data() {
    // when
    ingestionService.ingest();

    // then
    long count = repository.count();

    assertTrue(count > 0, "Expected crime records to be persisted into PostGIS");
  }
}
