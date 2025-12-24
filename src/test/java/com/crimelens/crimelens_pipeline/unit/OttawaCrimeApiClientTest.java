package com.crimelens.crimelens_pipeline.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.crimelens.crimelens_pipeline.client.OttawaCrimeApiClient;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

class OttawaCrimeApiClientTest {

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

    OttawaCrimeApiResponseDTO response = client.fetchCrimeData();
    System.out.println(response);

    assertNotNull(response, "Response should not be null!");
    assertNotNull(response.getFeatures(), "Features should not be null!");
    assertEquals(0, response.getFeatures().size(), "Features should not be empty!");
  }
}
