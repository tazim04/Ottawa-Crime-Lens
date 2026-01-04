package com.crimelens.crimelens_pipeline.dto;

import lombok.Data;

@Data
public class AttributesDTO {
  // Naming convention matches the output of the API (YTD dataset)

  // Identifiers
  private Long OBJECTID; // ArcGIS internal
  private String GO_Number; // stable police occurrence ID

  // Offence details
  private String Offence_Summary;
  private String Offence_Category;

  // Reported
  private Long Reported_Date; // epoch millis
  private Integer Reported_Year;
  private Integer Reported_Hour;

  // Occurred
  private Long Occurred_Date; // epoch millis
  private Integer Occurred_Year;
  private Integer Occurred_Hour;

  // Time metadata
  private String Time_of_Day;
  private String Week_Day;
  private String Day_of_Week;

  // Location descriptors
  private String Intersection;
  private String Neighbourhood;
  private String Sector;
  private String Division;
  private String Ward;
  private String Councillor;
}
