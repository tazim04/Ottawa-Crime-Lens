package com.crimelens.crimelens_pipeline.util;

import com.crimelens.crimelens_pipeline.dto.AttributesDTO;
import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.dto.GeometryDTO;

public final class DummyFeatureFactory {

  private DummyFeatureFactory() {}

  public static FeatureDTO createDummyFeature(Long objectId, long reportedDateMillis) {

    FeatureDTO feature = new FeatureDTO();

    GeometryDTO geometry = new GeometryDTO();
    geometry.setX(446000.0);
    geometry.setY(5039000.0);

    // Attributes (YTD schema)
    AttributesDTO attributes = new AttributesDTO();

    // Identifiers
    attributes.setOBJECTID(objectId);
    attributes.setGO_Number("GO-" + objectId); // stable business key for tests

    // Offence
    attributes.setOffence_Summary("Crime Against Persons");
    attributes.setOffence_Category("Assaults");

    // Reported
    attributes.setReported_Date(reportedDateMillis);
    attributes.setReported_Year(2026);
    attributes.setReported_Hour(14); // 2pm (0–23)

    // Occurred
    attributes.setOccurred_Date(reportedDateMillis - 3_600_000); // 1 hour earlier
    attributes.setOccurred_Year(2026);
    attributes.setOccurred_Hour(13);

    // Time metadata
    attributes.setTime_of_Day("Afternoon");
    attributes.setWeek_Day("Wednesday");
    attributes.setDay_of_Week("3");

    // Location descriptors
    attributes.setNeighbourhood("Centretown");
    attributes.setSector("Sector 33");
    attributes.setDivision("Central");
    attributes.setWard("Ward 14");
    attributes.setCouncillor("Jeff Leiper");
    attributes.setIntersection("BANK ST & SOMERSET ST W");

    feature.setGeometry(geometry);
    feature.setAttributes(attributes);

    return feature;
  }

  // Convenience overload
  public static FeatureDTO createDummyFeature() {
    return createDummyFeature(123456L, 1767243600000L);
  }
}
