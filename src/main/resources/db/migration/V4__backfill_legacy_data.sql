-- Years
UPDATE crime_records
SET reported_year = year
WHERE reported_year IS NULL;

UPDATE crime_records
SET occurred_year = EXTRACT(YEAR FROM occurred_date)
WHERE occurred_year IS NULL;

-- Convert TIME -> hour INT
UPDATE crime_records
SET reported_hour_int = EXTRACT(HOUR FROM reported_hour)
WHERE reported_hour IS NOT NULL;

UPDATE crime_records
SET occurred_hour_int = EXTRACT(HOUR FROM occurred_hour)
WHERE occurred_hour IS NOT NULL;
