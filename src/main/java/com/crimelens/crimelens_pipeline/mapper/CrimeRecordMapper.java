package com.crimelens.crimelens_pipeline.mapper;

import static com.crimelens.crimelens_pipeline.mapper.utils.MapperUtils.toLocalDate;
import static com.crimelens.crimelens_pipeline.mapper.utils.MapperUtils.toPoint4326;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrimeRecordMapper {

  // Map FeatureDTO -> CrimeRecord entity
  public CrimeRecord toEntity(FeatureDTO feature) {

    var attr = feature.getAttributes();
    var geom = feature.getGeometry();

    CrimeRecord record = new CrimeRecord();

    // GO number
    record.setGoNumber(attr.getGO_Number()); // NEW canonical business key

    // Reported times/dates
    record.setReportedDate(toLocalDate(attr.getReported_Date()));
    record.setReportedYear(attr.getReported_Year());
    record.setReportedHour(attr.getReported_Hour()); // already 0–23

    // Occurred times/dates
    record.setOccurredDate(toLocalDate(attr.getOccurred_Date()));
    record.setOccurredYear(attr.getOccurred_Year());
    record.setOccurredHour(attr.getOccurred_Hour());

    // Offence details
    record.setOffenceSummary(attr.getOffence_Summary());
    record.setOffenceCategory(attr.getOffence_Category());

    // Time metadata
    record.setTimeOfDay(attr.getTime_of_Day());
    record.setDayOfWeek(attr.getWeek_Day());

    // Location descriptors
    record.setIntersection(attr.getIntersection());
    record.setNeighbourhood(attr.getNeighbourhood());

    // Geometry
    if (geom != null && geom.getX() != null && geom.getY() != null) {
      Point point = toPoint4326(geom.getX(), geom.getY());
      record.setLocation(point);
    }

    return record;
  }
}
