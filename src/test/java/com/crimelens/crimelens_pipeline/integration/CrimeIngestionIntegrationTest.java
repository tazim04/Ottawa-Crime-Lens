package com.crimelens.crimelens_pipeline.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class CrimeIngestionIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgis =
      new PostgreSQLContainer<>(
              DockerImageName.parse("postgis/postgis:16-3.4").asCompatibleSubstituteFor("postgres"))
          .withDatabaseName("crimelens")
          .withUsername("test")
          .withPassword("test");

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
  void pipeline_is_idempotent_and_ignores_duplicates() {
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();

    when(apiClient.fetchCrimeData(anyInt(), anyInt()))
        .thenReturn(List.of(feature))
        .thenReturn(List.of());

    // First run
    var result1 = ingestionService.run();
    assertEquals(1, result1.inserted());
    assertEquals(1, repository.count());

    // Second run (same data)
    var result2 = ingestionService.run();
    assertEquals(0, result2.inserted());
    assertEquals(1, repository.count()); // no duplicates
  }
}
