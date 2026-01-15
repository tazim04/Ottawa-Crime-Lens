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

  private static final GeometryFactory GEOM_UTM =
      new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 0);

  private static volatile MathTransform TRANSFORM;

  private static MathTransform transform() {
    if (TRANSFORM == null) {
      synchronized (MapperUtils.class) {
        if (TRANSFORM == null) {
          try {
            // UTM: native axis order (Easting, Northing)
            var sourceCRS = CRS.decode("EPSG:32618");

            // WGS84: force lon, lat (XY)
            var targetCRS = CRS.decode("EPSG:4326", true);
            TRANSFORM = CRS.findMathTransform(sourceCRS, targetCRS, false);
          } catch (FactoryException e) {
            throw new IllegalStateException("CRS transform init failed", e);
          }
        }
      }
    }
    return TRANSFORM;
  }

  public static Point toPoint4326(double easting, double northing) {
    try {
      Point utm = GEOM_UTM.createPoint(new Coordinate(easting, northing));
      Geometry transformed = JTS.transform(utm, transform());
      transformed.setSRID(4326);
      return (Point) transformed;
    } catch (TransformException e) {
      throw new IllegalArgumentException("CRS transform failed", e);
    }
  }

  // Convert epoch millis -> LocalDate (Toronto time)
  public static LocalDate toLocalDate(Long epochMillis) {
    if (epochMillis == null) return null;
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("America/Toronto")).toLocalDate();
  }
}
