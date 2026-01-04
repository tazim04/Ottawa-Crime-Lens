ALTER TABLE crime_records
    ADD COLUMN id BIGINT GENERATED ALWAYS AS IDENTITY,
    ADD COLUMN go_number TEXT,
    ADD COLUMN reported_year INT,
    ADD COLUMN occurred_year INT,
    ADD COLUMN reported_hour_int INT,
    ADD COLUMN occurred_hour_int INT,
    ADD COLUMN sector TEXT,
    ADD COLUMN division TEXT,
    ADD COLUMN councillor TEXT;
