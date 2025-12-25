package com.crimelens.crimelens_pipeline.util;

import com.crimelens.crimelens_pipeline.dto.AttributesDTO;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.GeometryDTO;

public final class DummyFeatureFactory {

  private DummyFeatureFactory() {}

  public static FeatureDTO createDummyFeature(Long objectId, long repDateMillis) {
    FeatureDTO feature = new FeatureDTO();

    GeometryDTO geometry = new GeometryDTO();
    geometry.setX(-75.697193);
    geometry.setY(45.421530);

    AttributesDTO attributes = new AttributesDTO();
    attributes.setOBJECTID(objectId);
    attributes.setYEAR(2024);
    attributes.setREP_DATE(repDateMillis);
    attributes.setREP_HOUR(14);
    attributes.setOCC_DATE(repDateMillis - 3600000); // 1 hour earlier
    attributes.setOCC_HOUR(13);
    attributes.setWEEKDAY("WEDNESDAY");
    attributes.setOFF_SUM("ASSAULT");
    attributes.setOFF_CATEG("CRIMES AGAINST PERSON");
    attributes.setNB_NAME_EN("Centretown");
    attributes.setCENSUS_TRC("5050057.00");
    attributes.setTOD("AFTERNOON");
    attributes.setWARD("Ward 14");
    attributes.setINTERSECTION("BANK ST & SOMERSET ST W");

    feature.setGeometry(geometry);
    feature.setAttributes(attributes);

    return feature;
  }

  // overload for basic dummy feature creation
  public static FeatureDTO createDummyFeature() {
    return createDummyFeature(123456L, 1729036800000L);
  }
}
