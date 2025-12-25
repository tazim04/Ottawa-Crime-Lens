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

  @Id
  @Column(name = "id")
  private Long id; // From OBJECTID in the original API response

  @Column(name = "year")
  private Integer year;

  @Column(name = "reported_date")
  private LocalDateTime reportedDate;

  @Column(name = "reported_hour")
  private Integer reportedHour;

  @Column(name = "occurred_date")
  private LocalDateTime occurredDate;

  @Column(name = "occurred_hour")
  private Integer occurredHour;

  @Column(name = "day_of_week")
  private String dayOfWeek;

  @Column(name = "time_of_day")
  private String timeOfDay;

  @Column(name = "offence_summary")
  private String offenceSummary;

  @Column(name = "offence_category")
  private String offenceCategory;

  @Column(name = "neighbourhood")
  private String neighbourhood;

  @Column(name = "census_tract")
  private String censusTract;

  @Column(name = "ward")
  private String ward;

  @Column(name = "intersection")
  private String intersection;

  @Column(name = "location", columnDefinition = "geometry(Point, 4326)")
  private Point location; // PostGIS geospatial point
}
