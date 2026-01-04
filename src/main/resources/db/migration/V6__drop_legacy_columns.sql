ALTER TABLE crime_records
    DROP COLUMN incident_id,
    DROP COLUMN year,
    DROP COLUMN census_tract,
    DROP COLUMN reported_hour,
    DROP COLUMN occurred_hour;

ALTER TABLE crime_records
    RENAME COLUMN reported_hour_int TO reported_hour;

ALTER TABLE crime_records
    RENAME COLUMN occurred_hour_int TO occurred_hour;
