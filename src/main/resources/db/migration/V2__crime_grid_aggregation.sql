CREATE MATERIALIZED VIEW crime_stats_grid AS
SELECT
    ST_SnapToGrid(location, 0.01) AS grid,

    COUNT(*) AS total_crimes,

    COUNT(*) / GREATEST(
            DATE_PART('year', MAX(reported_date) - MIN(reported_date)),
            1
               ) AS avg_crimes_per_year,

    COUNT(*) FILTER (
        WHERE reported_date > NOW() - INTERVAL '1 year'
        ) AS crimes_last_year,

    MODE() WITHIN GROUP (ORDER BY offence_category) AS most_common_crime,

    MIN(reported_date) AS first_reported,
    MAX(reported_date) AS last_reported

FROM crime_records
GROUP BY grid;

CREATE INDEX IF NOT EXISTS idx_crime_stats_grid_geom
    ON crime_stats_grid
        USING GIST (grid);
