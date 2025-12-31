package com.crimelens.crimelens_pipeline.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ActiveProfiles("integration")
class CrimeIngestionIntegrationTest {

  // PostGIS container (real spatial DB)
  @Container
  static PostgreSQLContainer<?> postgis =
      new PostgreSQLContainer<>(
              DockerImageName.parse("postgis/postgis:16-3.4").asCompatibleSubstituteFor("postgres"))
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

  @MockitoBean private OttawaCrimeApiClient apiClient;

  @Test
  void pipeline_fetches_and_persists_crime_data() {
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();

    when(apiClient.fetchCrimeData(anyInt(), anyInt(), any()))
        .thenReturn(List.of(feature)) // first page
        .thenReturn(List.of()); // second page -> terminate loop

    ingestionService.run();

    long count = repository.count();
    assertTrue(count > 0, "Expected crime records to be persisted into PostGIS");
  }

  @Test
  void rerun_ignores_crimes_before_last_reported_date_and_inserts_only_new_ones() {

    // --- FIRST ingestion run ---
    long t1Millis = LocalDateTime.of(2024, 1, 1, 10, 0).toInstant(ZoneOffset.UTC).toEpochMilli();

    FeatureDTO crimeAtT1 = DummyFeatureFactory.createDummyFeature(1L, t1Millis);

    when(apiClient.fetchCrimeData(anyInt(), anyInt(), any()))
        .thenReturn(List.of(crimeAtT1)) // first page
        .thenReturn(List.of()); // stop pagination

    ingestionService.run();

    long countAfterFirstRun = repository.count();
    assertEquals(1, countAfterFirstRun, "Expected 1 crime after first ingestion");

    // --- SECOND ingestion run ---
    long t2Millis = LocalDateTime.of(2024, 1, 2, 12, 0).toInstant(ZoneOffset.UTC).toEpochMilli();

    FeatureDTO newCrime =
        DummyFeatureFactory.createDummyFeature(2L, t2Millis); // new ID, newer date

    /*
     * IMPORTANT:
     * We simulate correct API behavior:
     * only crimes AFTER lastRepDate are returned
     */
    when(apiClient.fetchCrimeData(anyInt(), anyInt(), any()))
        .thenReturn(List.of(newCrime)) // API returns only new crime
        .thenReturn(List.of());

    ingestionService.run();

    // --- THEN ---
    long finalCount = repository.count();
    assertEquals(2, finalCount, "Expected only the new crime to be inserted");

    // Verify the cursor advanced correctly
    assertTrue(
        repository.findLatestReportedDate().isAfter(LocalDateTime.of(2024, 1, 1, 10, 0)),
        "Expected lastRepDate to advance after rerun");
  }
}
