CREATE MATERIALIZED VIEW IF NOT EXISTS crime_stats_grid AS
SELECT
    ST_SnapToGrid(location, 0.01) AS grid,
    COUNT(*) AS total_crimes,
    MODE() WITHIN GROUP (ORDER BY offence_category) AS most_common_crime
FROM crime_records
GROUP BY grid;

CREATE INDEX IF NOT EXISTS idx_crime_stats_grid_geom
    ON crime_stats_grid
        USING GIST (grid);
