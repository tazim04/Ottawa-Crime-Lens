package com.crimelens.crimelens_pipeline.mapper;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrimeRecordMapper {

  private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

  // Map FeatureDTO -> CrimeRecord entity
  public CrimeRecord toEntity(FeatureDTO feature) {

    var attr = feature.getAttributes();
    var geom = feature.getGeometry();

    CrimeRecord record = new CrimeRecord();

    // GO number
    record.setGoNumber(attr.getGO_Number()); // NEW canonical business key

    // Reported times/dates
    record.setReportedDate(toLocalDateTime(attr.getReported_Date()));
    record.setReportedYear(attr.getReported_Year());
    record.setReportedHour(attr.getReported_Hour()); // already 0–23

    // Occurred times/dates
    record.setOccurredDate(toLocalDateTime(attr.getOccurred_Date()));
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
    record.setWard(attr.getWard());
    record.setCouncillor(attr.getCouncillor());

    // Geometry
    if (geom != null && geom.getX() != null && geom.getY() != null) {
      Point point = geometryFactory.createPoint(new Coordinate(geom.getX(), geom.getY()));
      record.setLocation(point);
    }

    return record;
  }

  // Convert epoch millis -> LocalDateTime (Toronto time)
  private LocalDateTime toLocalDateTime(Long epochMillis) {
    if (epochMillis == null) return null;

    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDateTime();
  }
}
