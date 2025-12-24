package com.crimelens.crimelens_pipeline.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

class OttawaCrimeApiClientTest {
  private static final LocalDateTime DATE_DEFAULT = LocalDateTime.of(1970, 1, 1, 0, 0);
  private static final int PAGE_SIZE = 1000;

  private MockWebServer server;
  private OttawaCrimeApiClient client;

  @BeforeEach
  void setup() throws IOException {
    server = new MockWebServer();
    server.start();

    WebClient webClient = WebClient.builder().baseUrl(server.url("/").toString()).build();

    client = new OttawaCrimeApiClient(webClient);
    ReflectionTestUtils.setField(client, "crimePath", "/query");
  }

  @Test
  void fetchCrimeDataParsesResponse() throws Exception {
    server.enqueue(
        new MockResponse()
            .setBody("{\"features\": []}")
            .addHeader("Content-Type", "application/json"));

    List<FeatureDTO> features = client.fetchCrimeData(0, PAGE_SIZE, DATE_DEFAULT);
    System.out.println(features);

    assertNotNull(features, "Features list should not be null!");
    assertEquals(0, features.size(), "Features list should not be empty!");
  }
}
