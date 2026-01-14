package com.crimelens.crimelens_pipeline.mapper.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.*;

@RequiredArgsConstructor
public class MapperUtils {

  private static final GeometryFactory GEOMETRY_FACTORY =
      new GeometryFactory(new PrecisionModel(), 32618);

  // Cached MathTransform
  private static volatile MathTransform TRANSFORM;

  private static MathTransform transform() {
    if (TRANSFORM == null) {
      synchronized (MapperUtils.class) {
        if (TRANSFORM == null) {
          try {
            TRANSFORM =
                CRS.findMathTransform(
                    CRS.decode("EPSG:32618", true), CRS.decode("EPSG:4326", true), true);
          } catch (FactoryException e) {
            throw new IllegalStateException("CRS transform init failed", e);
          }
        }
      }
    }
    return TRANSFORM;
  }

  // Convert epoch millis -> LocalDate (Toronto time)
  public static LocalDate toLocalDate(Long epochMillis) {
    if (epochMillis == null) return null;
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDate();
  }

  // Convert 32618 to 4326
  public static Point toPoint4326(double x, double y) {
    try {
      Point utm = GEOMETRY_FACTORY.createPoint(new Coordinate(x, y));
      utm.setSRID(32618);

      Geometry g = JTS.transform(utm, transform());
      g.setSRID(4326);
      return (Point) g;
    } catch (TransformException e) {
      throw new IllegalArgumentException("CRS transform failed", e);
    }
  }
}
