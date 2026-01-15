package com.crimelens.crimelens_pipeline.mapper.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapperUtils {
  // Convert epoch millis -> LocalDate (Toronto time)
  public static LocalDate toLocalDate(Long epochMillis) {
    if (epochMillis == null) return null;
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDate();
  }
}
