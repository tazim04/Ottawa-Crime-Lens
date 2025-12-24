package com.crimelens.crimelens_pipeline.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.repository.CrimeRecordRepository;
import com.crimelens.crimelens_pipeline.service.CrimeIngestionService;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
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
class CrimePipelineIntegrationTest {

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

    ingestionService.ingest();

    long count = repository.count();
    assertTrue(count > 0, "Expected crime records to be persisted into PostGIS");
  }
}
