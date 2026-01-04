ALTER TABLE crime_records
    DROP CONSTRAINT crime_records_pkey;

ALTER TABLE crime_records
    ADD CONSTRAINT crime_records_pkey PRIMARY KEY (id);
