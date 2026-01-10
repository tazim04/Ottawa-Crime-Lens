CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE crime_records (
   id BIGSERIAL PRIMARY KEY,

   go_number TEXT NOT NULL,

   reported_date TIMESTAMP,
   occurred_date TIMESTAMP,

   reported_year INT,
   occurred_year INT,

   reported_hour INT,
   occurred_hour INT,

   day_of_week TEXT,
   time_of_day TEXT,

   offence_summary TEXT,
   offence_category TEXT,

   intersection TEXT,
   neighbourhood TEXT,

   source TEXT NOT NULL DEFAULT 'OFFICIAL',

   location GEOMETRY(Point, 4326)
);

-- Enforce natural-key uniqueness
ALTER TABLE crime_records
    ADD CONSTRAINT uq_crime_records_go_number UNIQUE (go_number);

-- Indexes
CREATE INDEX idx_crime_location
    ON crime_records
        USING GIST (location);

CREATE INDEX idx_crime_reported_date
    ON crime_records (reported_date);

CREATE INDEX idx_crime_occurred_date
    ON crime_records (occurred_date);
