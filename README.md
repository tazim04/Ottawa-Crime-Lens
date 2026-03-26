# Ottawa CrimeLens — System Overview

A geospatial crime analysis platform that ingests, processes, and detects anomalous crime activity using machine learning.

---

## Overview

Ottawa CrimeLens is a full-stack distributed system designed to analyze Ottawa crime data across spatial grids and surface anomalous activity patterns.

The platform is composed of multiple services working together:

- Data ingestion pipeline (Spring Boot)
- ML anomaly detection pipeline (Python + Isolation Forest)
- Backend API service (Spring Boot)
- Frontend dashboard (React + TypeScript)

---

## Repositories

| Component | Description | Link |
|----------|------------|------|
| Frontend | User-facing dashboard for visualizing crime data | https://github.com/tazim04/ottawa-crime-lens-frontend |
| Backend APIs | REST APIs for querying processed crime data | https://github.com/tazim04/Ottawa-Crime-Lens-Query |
| Ingestion Pipeline | Scheduled pipeline to ingest and store crime data | https://github.com/tazim04/Ottawa-Crime-Lens-Pipeline |
| ML Pipeline | Detects anomalous crime patterns across grid cells | https://github.com/tazim04/ottawa-crimelens-ml |

---

## System Flow

1. EventBridge triggers a Step Functions workflow daily
2. Step Functions runs the ingestion pipeline first
3. Ingestion service fetches and stores crime data in PostgreSQL/PostGIS
4. Step Functions then runs the ML scoring pipeline
5. ML pipeline computes anomaly scores per grid cell
6. Backend API serves processed data
7. Frontend visualizes anomalies on an interactive map (maplibre)

---

## ML Service Workflow (Short Overview)

1. **Input from CrimeLens data**  
	The ML service reads the latest cleaned and grid-aggregated crime records produced by the ingestion pipeline.

2. **Feature building**  
	For each grid cell and time window, the pipeline builds numeric features that summarize crime behavior (for example: incident counts, category mix, recent trend/change, and spatial context). These features convert raw events into a model-ready vector per grid/time slice.

3. **Training with Isolation Forest**  
	The model is trained in an unsupervised way on historical feature vectors. Isolation Forest learns what "normal" crime patterns look like by randomly partitioning the feature space; points that are isolated in fewer splits are treated as more anomalous.

4. **Scoring**  
	The trained model scores the latest feature vectors and outputs an anomaly score (and anomaly flag) per grid cell/time slice, where more extreme scores indicate less typical crime activity versus historical baseline.

5. **Write-back to database**  
	Final scores are persisted to PostgreSQL/PostGIS in the `crime_anomaly_scores` table. The backend APIs service then reads these scored results so the frontend can render anomaly triage labels.

---

## Architecture

![System Diagram](./imgs/crimelens_system.png)

---

## Tech Stack

- **Frontend:** React, TypeScript  
- **Backend:** Spring Boot (Java)  
- **ML:** Python, scikit-learn (Isolation Forest)
- **Database:** PostgreSQL + PostGIS (Neon)
- **Infrastructure:** Docker, AWS ECS Fargate, EventBridge, Step Functions  

---

## Deployment

- All services are containerized using Docker
- Images are stored in AWS ECR
- ECS Fargate is used to run scheduled and on-demand tasks
- EventBridge triggers the Step Functions orchestration workflow as a daily cron job
- Step Functions manages multi-step pipeline (ingestion -> ML scoring)

---

## Key Design Decisions

- Used **Isolation Forest** for unsupervised anomaly detection on crime patterns
- Designed **daily batch scoring** instead of real-time processing for simplicity and cost efficiency
- Leveraged **PostGIS** for spatial aggregation and grid-based analysis
- Separated ingestion, ML, and APIs into independent services for scalability and modularity
- Deployed Backend APIs service locally on-prem to avoid always-on cloud costs

---

## Live Demo

https://www.ottawacrimelens.ca/

---

## Author

Built by Tazim Khan
