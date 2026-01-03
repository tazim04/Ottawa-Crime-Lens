package com.crimelens.crimelens_pipeline.mapper;

import com.crimelens.crimelens_pipeline.dto.FeatureDTO;
import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

  // Map OttawaCrimeApiResponse -> CrimeRecord entity for DB
  public CrimeRecord toEntity(FeatureDTO feature) {

    var attr = feature.getAttributes();
    var geom = feature.getGeometry();

    CrimeRecord record = new CrimeRecord();

    record.setIncidentId(attr.getOBJECTID());

    record.setYear(attr.getYEAR());
    record.setReportedDate(toLocalDateTime(attr.getREP_DATE()));
    record.setReportedHour(hhmmToLocalTime(attr.getREP_HOUR()));

    record.setOccurredDate(toLocalDateTime(attr.getOCC_DATE()));
    record.setOccurredHour(hhmmToLocalTime(attr.getOCC_HOUR()));

    record.setDayOfWeek(attr.getWEEKDAY());

    record.setOffenceSummary(attr.getOFF_SUM());
    record.setOffenceCategory((attr.getOFF_CATEG()));

    record.setNeighbourhood(attr.getNB_NAME_EN());
    record.setCensusTract(attr.getCENSUS_TRC());
    record.setWard(attr.getWARD());
    record.setIntersection(attr.getINTERSECTION());
    record.setTimeOfDay(attr.getTOD());

    // Create a PostGIS point
    if (geom != null && geom.getX() != null && geom.getY() != null) {
      Point point =
          geometryFactory.createPoint(
              new Coordinate(
                  geom.getX(), // longitude
                  geom.getY() // latitude
                  ));
      record.setLocation(point);
    }

    return record;
  }

  // Convert epochMillis timestamp into LocalDateTime format
  private LocalDateTime toLocalDateTime(Long epochMillis) {
    if (epochMillis == null) return null;

    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDateTime();
  }

  // Convert Integer representation of hour to LocalTime
  private LocalTime hhmmToLocalTime(Integer hhmm) {
    if (hhmm == null) return null;

    int hour = hhmm / 100;
    int minute = hhmm % 100;

    // defensive check
    if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
      throw new IllegalArgumentException("Invalid HHMM time: " + hhmm);
    }

    return LocalTime.of(hour, minute);
  }
}
