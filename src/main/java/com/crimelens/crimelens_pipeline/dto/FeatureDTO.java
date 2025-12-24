package com.crimelens.crimelens_pipeline.dto;

import lombok.Data;

@Data
public class FeatureDTO {
  private AttributesDTO attributes;
  private GeometryDTO geometry;
}
