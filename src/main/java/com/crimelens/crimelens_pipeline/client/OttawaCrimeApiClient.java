package com.crimelens.crimelens_pipeline.client;

import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OttawaCrimeApiClient {

  private final WebClient ottawaPoliceApiClient;

  @Value("${ottawa-police.api.crime-path}")
  private String crimePath;

  // Function to fetch crime data from the public Ottawa Police API
  public OttawaCrimeApiResponseDTO fetchCrimeData() {
    return ottawaPoliceApiClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(crimePath)
                    .queryParam("where", "1=1")
                    .queryParam("outFields", "*")
                    .queryParam("outSR", "4326")
                    .queryParam("f", "json")
                    .build())
        .retrieve()
        .bodyToMono(OttawaCrimeApiResponseDTO.class)
        .block();
  }
}
