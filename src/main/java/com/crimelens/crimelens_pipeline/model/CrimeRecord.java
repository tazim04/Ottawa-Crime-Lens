package com.crimelens.crimelens_pipeline.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "crime_records")
@Data
public class CrimeRecord {

  @Id private Long id; // From OBJECTID in the original API response

  private Integer year;
  private LocalDateTime reportedDate;
  private Integer reportedHour;

  private LocalDateTime occurredDate;
  private Integer occurredHour;

  private String dayOfWeek; // maps to -> WEEKDAY

  private String offenceSummary;
  private String offenceCategory;

  private String neighbourhood;
  private String sector;
  private String division;
  private String censusTract;
  private String timeOfDay;
  private String ward;
  private String councillor;
  private String intersection;

  @Column(columnDefinition = "geometry(Point, 4326)")
  private Point location; // PostGIS geospatial point
}
