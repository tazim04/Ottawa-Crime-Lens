package com.crimelens.crimelens_pipeline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient ottawaPoliceApiClient() {
    return WebClient.builder()
        .baseUrl("https://services7.arcgis.com/2vhcNzw0NfUwAD3d")
        .exchangeStrategies( // Increase max buffer size for extremely large amounts of data (200k+)
            ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024))
                .build())
        .defaultHeader("Accept", "application/json")
        .build();
  }
}
