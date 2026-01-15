package com.crimelens.crimelens_pipeline.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.mapper.CrimeRecordMapper;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import com.crimelens.crimelens_pipeline.util.DummyFeatureFactory;
import org.junit.jupiter.api.Test;

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
}
