# Manus.im SaaS Management App

This directory contains a minimal skeleton for a collaborative management application powered by Manus.im.

## Overview

The goal is to provide inter-department communication, action tracking, meeting organization and PDCA problem solving with real-time dashboards.

### Components

- **Backend**: Java & Spring Boot with a PostgreSQL database.
- **Frontend**: Angular application for the user interface.
- **API**: REST endpoints secured with JWT for third‑party integrations.

Each subdirectory includes a basic scaffold to start development.

## Testing the Skeleton

1. **Start the backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
   The service listens on `http://localhost:8081`.
2. **Check the health endpoint**:
   ```bash
   curl http://localhost:8081/api/health
   ```
   You should see `OK`.
3. **Start the frontend placeholder** (optional):
   ```bash
   cd ../frontend
   npm start
   ```

Automated unit tests are not yet implemented.
