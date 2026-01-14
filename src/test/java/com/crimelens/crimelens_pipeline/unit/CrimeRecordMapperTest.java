package com.crimelens.crimelens_pipeline.unit;

import static com.crimelens.crimelens_pipeline.mapper.utils.MapperUtils.toPoint4326;
import static org.junit.jupiter.api.Assertions.*;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

class CrimeRecordMapperTest {

  private final CrimeRecordMapper mapper = new CrimeRecordMapper();

  @Test
  void mapsGeometryCorrectly() {
    // Dummy feature
    FeatureDTO dto = DummyFeatureFactory.createDummyFeature();

    CrimeRecord record = mapper.toEntity(dto);

    assertInstanceOf(CrimeRecord.class, record, "`record` should be of type CrimeRecord!");
    assertNotNull(record.getLocation());
    assertEquals(4326, record.getLocation().getSRID());
  }

  @Test
  void convertsUtmToWgs84Correctly() {
    FeatureDTO feature = DummyFeatureFactory.createDummyFeature();

    Point point = toPoint4326(feature.getGeometry().getX(), feature.getGeometry().getY());

    // SRID must be WGS84
    assertEquals(4326, point.getSRID());

    // Coordinates must look like lat/lon (Ottawa-ish)
    double lon = point.getX();
    double lat = point.getY();

    assertTrue(lon >= -76.9 && lon <= -75.3, "Longitude not in Ottawa range");
    assertTrue(lat >= 45.1 && lat <= 45.6, "Latitude not in Ottawa range");
  }
}
