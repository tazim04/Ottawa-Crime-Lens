package com.crimelens.crimelens_pipeline.model;

import com.crimelens.crimelens_pipeline.enums.CrimeSource;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "crime_records")
@Data
public class CrimeRecord {

  // Primary Key (DB-generated, no business meaning)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  // Police report identifier
  @Column(name = "go_number", unique = false)
  private String goNumber; // Nullable for legacy data (2018 - 2024)

  // Reported times/dates
  @Column(name = "reported_date")
  private LocalDateTime reportedDate;

  @Column(name = "reported_year")
  private Integer reportedYear;

  @Column(name = "reported_hour")
  private Integer reportedHour; // 0–23 (NOT LocalTime)

  // Occurred times/dates
  @Column(name = "occurred_date")
  private LocalDateTime occurredDate;

  @Column(name = "occurred_year")
  private Integer occurredYear;

  @Column(name = "occurred_hour")
  private Integer occurredHour; // 0–23

  // Time metadata
  @Column(name = "day_of_week")
  private String dayOfWeek;

  @Column(name = "time_of_day")
  private String timeOfDay;

  // Offence details
  @Column(name = "offence_summary")
  private String offenceSummary;

  @Column(name = "offence_category")
  private String offenceCategory;

  // Location descriptors
  @Column(name = "intersection")
  private String intersection;

  @Column(name = "neighbourhood")
  private String neighbourhood;

  @Column(name = "ward")
  private String ward;

  @Column(name = "councillor")
  private String councillor;

  // Source
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CrimeSource source = CrimeSource.OFFICIAL;

  // Geometry
  @Column(name = "location", columnDefinition = "geometry(Point, 4326)")
  private Point location;
}
