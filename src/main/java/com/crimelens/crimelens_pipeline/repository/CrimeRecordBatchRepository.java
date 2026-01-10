package com.crimelens.crimelens_pipeline.repository;

import com.crimelens.crimelens_pipeline.model.CrimeRecord;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrimeRecordBatchRepository {

  private final JdbcTemplate jdbcTemplate;

  public int insertBatchIgnore(List<CrimeRecord> records) {
    if (records.isEmpty()) return 0;

    String sql = buildSql(records.size());

    Object[] params = new Object[records.size() * 16];
    int i = 0;

    for (CrimeRecord r : records) {
      params[i++] = r.getGoNumber();
      params[i++] = r.getReportedDate();
      params[i++] = r.getReportedYear();
      params[i++] = r.getReportedHour();
      params[i++] = r.getOccurredDate();
      params[i++] = r.getOccurredYear();
      params[i++] = r.getOccurredHour();
      params[i++] = r.getDayOfWeek();
      params[i++] = r.getTimeOfDay();
      params[i++] = r.getOffenceSummary();
      params[i++] = r.getOffenceCategory();
      params[i++] = r.getIntersection();
      params[i++] = r.getNeighbourhood();
      params[i++] = r.getSource().name();

      Point p = r.getLocation();
      if (p != null) {
        params[i++] = p.getX(); // lon
        params[i++] = p.getY(); // lat
      } else {
        params[i++] = null;
        params[i++] = null;
      }
    }

    return jdbcTemplate.update(sql, params);
  }

  private String buildSql(int rows) {
    String valueGroup =
        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))";

    return """
      INSERT INTO crime_records (
        go_number,
        reported_date, reported_year, reported_hour,
        occurred_date, occurred_year, occurred_hour,
        day_of_week, time_of_day,
        offence_summary, offence_category,
        intersection, neighbourhood,
        source, location
      )
      VALUES
      """
        + String.join(",", Collections.nCopies(rows, valueGroup))
        + " ON CONFLICT (go_number) DO NOTHING";
  }
}
