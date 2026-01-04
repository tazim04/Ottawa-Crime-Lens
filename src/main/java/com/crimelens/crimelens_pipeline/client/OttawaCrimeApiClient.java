package com.crimelens.crimelens_pipeline.client;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class OttawaCrimeApiClient {

  private final WebClient ottawaPoliceApiClient;

  @Value("${ottawa-police.api.crime-path}")
  private String crimePath;

  // Function to fetch crime data from the public Ottawa Police API, implements pagination to handle
  // the 200k+ records
  public List<FeatureDTO> fetchCrimeData(int offset, int pageSize, LocalDateTime lastRepDate) {
    String arcgisDate =
        lastRepDate.atZone(ZoneOffset.UTC).toLocalDateTime().toString().replace("T", " ");

    String whereClause = "Reported_Date > " + arcgisDate;

    OttawaCrimeApiResponseDTO response =
        ottawaPoliceApiClient
            .get()
            .uri(
                uriBuilder -> {
                  var uri =
                      uriBuilder
                          .path(crimePath)
                          .queryParam("where", whereClause)
                          .queryParam("outFields", "*")
                          .queryParam("outSR", "4326")
                          .queryParam("f", "json")
                          .queryParam("resultOffset", offset)
                          .queryParam("resultRecordCount", pageSize)
                          .queryParam("returnExceededLimitFeatures", true)
                          .queryParam("returnGeometry", true)
                          //                          .queryParam("orderByFields", "Reported_Date
                          // ASC")
                          .build();
                  log.debug("ArcGIS request URI: {}", uri);
                  return uri;
                })
            .retrieve()
            .bodyToMono(OttawaCrimeApiResponseDTO.class)
            .block();

    return response != null && response.getFeatures() != null ? response.getFeatures() : List.of();
  }
}
