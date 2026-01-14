DROP MATERIALIZED VIEW IF EXISTS crime_stats_grid;

ALTER TABLE crime_records
    ALTER COLUMN occurred_date TYPE DATE
        USING occurred_date::DATE;

ALTER TABLE crime_records
    ALTER COLUMN reported_date TYPE DATE
        USING reported_date::DATE;

CREATE MATERIALIZED VIEW crime_stats_grid AS
SELECT
    hashtext(ST_AsText(ST_SnapToGrid(location, 0.01)))::bigint AS id,
    ST_SnapToGrid(location, 0.01) AS grid,

    COUNT(*) AS total_crimes,

    COUNT(*) / GREATEST(
        ((MAX(reported_date) - MIN(reported_date)) / 365.25),
        1
   ) AS avg_crimes_per_year,

    COUNT(*) FILTER (
        WHERE reported_date >= CURRENT_DATE - INTERVAL '1 year'
    ) AS crimes_last_year,

    COUNT(*) FILTER (
        WHERE reported_date >= CURRENT_DATE - INTERVAL '5 years'
    ) AS crimes_last_5_years,

    COUNT(*) FILTER (
        WHERE reported_date >= CURRENT_DATE - INTERVAL '10 years'
    ) AS crimes_last_10_years,

    MODE() WITHIN GROUP (ORDER BY offence_category)
       AS most_common_crime_all_time,

    MODE() WITHIN GROUP (ORDER BY offence_category)
    FILTER (WHERE reported_date >= CURRENT_DATE - INTERVAL '1 year')
       AS most_common_crime_last_year,

    MODE() WITHIN GROUP (ORDER BY offence_category)
    FILTER (WHERE reported_date >= CURRENT_DATE - INTERVAL '5 years')
       AS most_common_crime_last_5_years,

    MODE() WITHIN GROUP (ORDER BY offence_category)
    FILTER (WHERE reported_date >= CURRENT_DATE - INTERVAL '10 years')
       AS most_common_crime_last_10_years,

    MIN(reported_date) AS first_reported,
    MAX(reported_date) AS last_reported

FROM crime_records
WHERE location IS NOT NULL
GROUP BY grid;

CREATE INDEX idx_crime_stats_grid_geom
    ON crime_stats_grid
        USING GIST (grid);

CREATE UNIQUE INDEX idx_crime_stats_grid_id
    ON crime_stats_grid (id);
