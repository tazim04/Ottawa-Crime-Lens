# Ottawa CrimeLens — System Overview

A geospatial crime analysis platform that ingests, processes, and detects anomalous crime activity using machine learning.

---

## Overview

Ottawa CrimeLens is a full-stack distributed system designed to analyze crime data across spatial grids and surface anomalous activity patterns.

The platform is composed of multiple services working together:

- Data ingestion pipeline (Spring Boot)
- ML anomaly detection pipeline (Python + Isolation Forest)
- Backend API (Spring Boot)
- Frontend dashboard (React + TypeScript)

---

## Repositories

| Component | Description | Link |
|----------|------------|------|
| Frontend | User-facing dashboard for visualizing crime data | https://github.com/your-username/frontend-repo |
| Backend API | REST API for querying processed crime data | https://github.com/your-username/backend-repo |
| Ingestion Pipeline | Scheduled pipeline to ingest and store crime data | https://github.com/your-username/ingestion-repo |
| ML Pipeline | Detects anomalous crime patterns across grid cells | https://github.com/your-username/ml-repo |

---

## System Flow

1. EventBridge triggers the ingestion pipeline daily
2. Ingestion service fetches and stores crime data in PostgreSQL/PostGIS
3. Step Functions orchestrates the ML scoring pipeline
4. ML pipeline computes anomaly scores per grid cell
5. Backend API serves processed data
6. Frontend visualizes anomalies on an interactive map

---

## Architecture

![System Diagram](./architecture/system-diagram.png)

---

## Tech Stack

- **Frontend:** React, TypeScript  
- **Backend:** Spring Boot (Java)  
- **ML:** Python, scikit-learn (Isolation Forest)  
- **Database:** PostgreSQL + PostGIS  
- **Infrastructure:** Docker, AWS ECS Fargate, EventBridge, Step Functions  

---

## Deployment

- All services are containerized using Docker
- Images are stored in AWS ECR
- ECS Fargate is used to run scheduled and on-demand tasks
- EventBridge triggers ingestion and orchestration workflows
- Step Functions manages multi-step pipelines (ingestion → ML scoring)

---

## Key Design Decisions

- Used **Isolation Forest** for unsupervised anomaly detection on crime patterns
- Designed **daily batch scoring** instead of real-time processing for simplicity and cost efficiency
- Leveraged **PostGIS** for spatial aggregation and grid-based analysis
- Separated ingestion, ML, and API into independent services for scalability

---

## Future Improvements

- Real-time data ingestion and streaming pipeline
- More advanced anomaly detection models
- Alerting system for high-risk areas
- Improved model explainability and visualization

---

## Live Demo

https://www.ottawacrimelens.ca/

---

## Author

Built by Kylan
