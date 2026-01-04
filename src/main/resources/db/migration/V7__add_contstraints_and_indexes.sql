CREATE UNIQUE INDEX uq_crime_go_number
    ON crime_records (go_number)
    WHERE go_number IS NOT NULL;

CREATE INDEX idx_crime_reported_date
    ON crime_records (reported_date);

CREATE INDEX IF NOT EXISTS idx_crime_location
    ON crime_records
        USING GIST (location);
