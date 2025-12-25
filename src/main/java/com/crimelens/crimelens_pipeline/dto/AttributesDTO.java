package com.crimelens.crimelens_pipeline.dto;

import lombok.Data;

@Data
public class AttributesDTO {
  private Long OBJECTID;
  private Integer YEAR;
  private Long REP_DATE;
  private Integer REP_HOUR;
  private Long OCC_DATE;
  private Integer OCC_HOUR;
  private String WEEKDAY;
  private String OFF_SUM;
  private String OFF_CATEG;
  private String NB_NAME_EN;
  private String CENSUS_TRC;
  private String TOD;
  private String WARD;
  private String INTERSECTION;
}
