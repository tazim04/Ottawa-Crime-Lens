# CrimeLens Pipeline

Spring Boot microservice responsible for fetching, normalizing, and persisting
public crime data for the CrimeLens platform.

This service ingests data from the Ottawa Police's [Criminal Offesnes Open Data public API](https://data.ottawapolice.ca/datasets/0356deb79cfd4488956116fda366df38_0/explore?showTable=true), transforms it into
a normalized domain model, and stores it in a PostGIS database.

---

## Running the Application (with Docker)

This project is designed to be run using Docker Compose for local development.

### To Start the application:
From the project root:

Build the project:
```Bash
mvn clean install -DskipTests
```

And run Docker compose:
```Bash
docker compose up --build
```

This will:
* Start a PostGIS-enabled PostgreSQL database
* Build and run the CrimeLens pipeline service
* Automatically apply Flyway migrations

### To stop the application:
```Bash
docker compose down
```

## Running Tests

### Run all Tests (unit + integration)
```Bash
mvn verify
```

This will:
* Run unit tests
* Spin up a PostGIS Testcontainer for integration tests
* Validate ingestion pipeline and rerun behavior
* Enforce formatting via Spotless

> Note: Docker must be running for integration tests (Testcontainers).

## Code Formatting

This project uses **Spotless** with **Google Java Format**.

Format code:
```Bash
mvn spotless:apply
```

Check code:
```Bash
mvn spotless:check
```