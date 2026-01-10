ALTER SEQUENCE crime_records_id_seq INCREMENT BY 1000;

SELECT setval(
        'crime_records_id_seq',
        (SELECT COALESCE(MAX(id), 0) FROM crime_records) + 1000
    );

ALTER SEQUENCE crime_records_id_seq OWNED BY crime_records.id;