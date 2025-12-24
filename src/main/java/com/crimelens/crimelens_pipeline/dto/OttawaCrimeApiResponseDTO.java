package com.crimelens.crimelens_pipeline.dto;

import java.util.List;
import lombok.Data;

@Data
public class OttawaCrimeApiResponseDTO {
  // Represents the external Ottawa Police API response structure
  private List<FeatureDTO> features;
}
