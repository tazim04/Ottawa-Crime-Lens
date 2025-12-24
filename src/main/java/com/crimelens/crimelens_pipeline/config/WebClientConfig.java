package com.crimelens.crimelens_pipeline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient ottawaPoliceApiClient() {
    return WebClient.builder()
        .baseUrl("https://services7.arcgis.com/2vhcNzw0NfUwAD3d")
        .defaultHeader("Accept", "application/json")
        .build();
  }
}
