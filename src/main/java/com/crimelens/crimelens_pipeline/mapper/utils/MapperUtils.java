package com.crimelens.crimelens_pipeline.mapper.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapperUtils {

  // Convert epoch millis -> LocalDateTime (Toronto time)
  public static LocalDateTime toLocalDateTime(Long epochMillis) {
    if (epochMillis == null) return null;
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDateTime();
  }
}
