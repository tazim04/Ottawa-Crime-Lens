package com.crimelens.crimelens_pipeline.util;

import com.crimelens.crimelens_pipeline.dto.AttributesDTO;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.GeometryDTO;

public final class DummyFeatureFactory {

  private DummyFeatureFactory() {}

  public static FeatureDTO createDummyFeature() {
    FeatureDTO feature = new FeatureDTO();

    GeometryDTO geometry = new GeometryDTO();
    geometry.setX(-75.697193);
    geometry.setY(45.421530);

    AttributesDTO attributes = new AttributesDTO();
    attributes.setOBJECTID(123456L);
    attributes.setYEAR(2024);
    attributes.setREP_DATE(1729036800000L);
    attributes.setREP_HOUR(14);
    attributes.setOCC_DATE(1729033200000L);
    attributes.setOCC_HOUR(13);
    attributes.setWEEKDAY("WEDNESDAY");
    attributes.setOFF_SUM("ASSAULT");
    attributes.setOFF_CATEG("CRIMES AGAINST PERSON");
    attributes.setNB_NAME_EN("Centretown");
    attributes.setSECTOR("CENTRAL");
    attributes.setDIVISION("D1");
    attributes.setCENSUS_TRC("5050057.00");
    attributes.setTOD("AFTERNOON");
    attributes.setWARD("Ward 14");
    attributes.setCOUNCILLOR("Rawlson King");
    attributes.setINTERSECTION("BANK ST & SOMERSET ST W");

    feature.setGeometry(geometry);
    feature.setAttributes(attributes);

    return feature;
  }
}
