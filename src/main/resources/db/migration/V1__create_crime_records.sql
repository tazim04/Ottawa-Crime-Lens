CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE IF NOT EXISTS crime_records (
    incident_id BIGINT PRIMARY KEY,
    year INT,
    reported_date TIMESTAMP,
    reported_hour INT,
    occurred_date TIMESTAMP,
    occurred_hour INT,
    day_of_week TEXT,
    time_of_day TEXT,
    offence_summary TEXT,
    offence_category TEXT,
    neighbourhood TEXT,
    ward TEXT,
    intersection TEXT,
    census_tract TEXT,
    location GEOMETRY(Point, 4326)
);

CREATE INDEX IF NOT EXISTS idx_crime_location
    ON crime_records
    USING GIST (location);
