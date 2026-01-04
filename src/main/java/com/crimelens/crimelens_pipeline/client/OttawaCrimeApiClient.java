package com.crimelens.crimelens_pipeline.client;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.OttawaCrimeApiResponseDTO;
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

  // Function to fetch crime data from the public Ottawa Police API, implements pagination
  public List<FeatureDTO> fetchCrimeData(int offset, int pageSize) {

    OttawaCrimeApiResponseDTO response =
        ottawaPoliceApiClient
            .get()
            .uri(
                uriBuilder -> {
                  var uri =
                      uriBuilder
                          .path(crimePath)
                          .queryParam("where", "1=1")
                          .queryParam("outFields", "*")
                          .queryParam("returnGeometry", true)
                          .queryParam("resultRecordCount", pageSize)
                          .queryParam("resultOffset", offset)
                          .queryParam("f", "json")
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
