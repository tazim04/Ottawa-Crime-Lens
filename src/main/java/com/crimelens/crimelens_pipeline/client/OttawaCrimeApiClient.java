package com.crimelens.crimelens_pipeline.client;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
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

  // Function to fetch crime data from the public Ottawa Police API, implements pagination to handle
  // the 200k+ records
  public List<FeatureDTO> fetchCrimeData(int offset, int pageSize, LocalDateTime lastRepDate) {
    long lastRepDateMillis = lastRepDate.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

    OttawaCrimeApiResponseDTO response =
        ottawaPoliceApiClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path(crimePath)
                        .queryParam("where", "REP_DATE > " + lastRepDateMillis)
                        .queryParam("where", "1=1")
                        .queryParam("outFields", "*")
                        .queryParam("outSR", "4326")
                        .queryParam("f", "json")
                        .queryParam("resultOffset", offset)
                        .queryParam("resultRecordCount", pageSize)
                        .build())
            .retrieve()
            .bodyToMono(OttawaCrimeApiResponseDTO.class)
            .block();

    System.out.println("response = " + response);

    return response != null && response.getFeatures() != null ? response.getFeatures() : List.of();
  }
}
