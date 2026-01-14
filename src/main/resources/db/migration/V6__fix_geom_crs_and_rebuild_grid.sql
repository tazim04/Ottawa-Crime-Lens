DROP MATERIALIZED VIEW IF EXISTS crime_stats_grid;

ALTER TABLE crime_records
    ALTER COLUMN location
        TYPE geometry(Point, 4326)
        USING
        CASE
            WHEN location IS NULL THEN NULL
            WHEN ST_SRID(location) = 32618
                THEN ST_Transform(location, 4326)
            WHEN ST_SRID(location) = 4326
                THEN location
            ELSE
                ST_Transform(ST_SetSRID(location, 32618), 4326)
            END;

DO $$
    BEGIN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'enforce_location_srid_4326'
        ) THEN
            ALTER TABLE crime_records
                ADD CONSTRAINT enforce_location_srid_4326
                    CHECK (location IS NULL OR ST_SRID(location) = 4326);
        END IF;
    END $$;

DROP INDEX IF EXISTS idx_crime_location;

CREATE INDEX idx_crime_location
    ON crime_records
        USING GIST (location);

CREATE MATERIALIZED VIEW crime_stats_grid AS
SELECT
    hashtext(ST_AsText(ST_SnapToGrid(location, 0.01)))::bigint AS id,
    ST_SnapToGrid(location, 0.01) AS grid,
    COUNT(*) AS total_crimes,
    COUNT(*) / GREATEST(
            (MAX(reported_date) - MIN(reported_date)) / 365.25,
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
