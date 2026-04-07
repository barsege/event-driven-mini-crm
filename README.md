# Event Driven Mini CRM

Event Driven Mini CRM is a backend project built with **Java 17, Spring Boot, Kafka and PostgreSQL** to demonstrate how **event-driven architecture** works in a real system.

The application simulates a CRM workflow where **qualifying a Lead produces a Kafka event which is consumed to create an Opportunity**.

The project also demonstrates important backend patterns such as:

* Event publishing
* Kafka consumers
* Idempotent event processing
* Dead letter queue (DLT)
* Failed event persistence
* Failed event reprocessing

---

# Architecture

The application follows an **event-driven architecture** where services communicate through Kafka events instead of direct service calls.

When a Lead becomes qualified, the system publishes a `LeadQualifiedEvent`.
A Kafka consumer listens to this event and creates an Opportunity.

This approach improves:

* scalability
* decoupling between services
* fault tolerance

---

# Architecture Flow

```
Client
  |
  v
Lead REST API (Spring Boot)
  |
  | LeadQualifiedEvent
  v
Kafka Topic (crm.lead-events)
  |
  v
Opportunity Event Consumer
  |
  v
PostgreSQL
```

---

# Tech Stack

* Java 17
* Spring Boot 3
* PostgreSQL
* Apache Kafka
* Spring Data JPA (Hibernate)
* Docker Compose (Kafka infrastructure)
* JUnit
* Testcontainers

---

# Core Concepts Demonstrated

This project focuses on several important backend architecture concepts.

### Event Driven Communication

Instead of synchronous service calls, the system communicates through Kafka events.

Example:

Lead qualified → event published → opportunity created

---

### Idempotent Event Processing

Kafka may deliver the same event more than once.

To avoid duplicate processing the application stores processed events in a table:

`processed_events`

Before processing an event the system checks if the eventId was already processed.

If yes → ignore
If no → process and store eventId

---

### Dead Letter Queue (DLT)

If event processing fails after retries the event is sent to a **Dead Letter Queue**.

Failed events are stored in:

`failed_events`

This enables:

* debugging
* manual replay
* operational recovery

---

### Failed Event Reprocessing

The application provides endpoints that allow failed events to be **reprocessed manually**.

This simulates real-world production workflows where engineers replay events after fixing a bug.

---

# Database Schema

The application uses PostgreSQL.

Main tables:

### leads

```
id (UUID)
first_name
last_name
email
phone
status
converted_opportunity_id
created_at
```

---

### opportunities

```
id (UUID)
lead_id (UUID)
created_at
```

---

### processed_events

```
event_id
event_type
processed_at
```

---

### failed_events

```
id
event_id
event_type
payload
error_message
created_at
```

Relationship:

```
Lead
  |
  | 1
  |
  |------< Opportunity
```

---

# API Endpoints

## Health Check

Check if the application is running.

GET /api/ping

Response

```
pong
```

---

# Lead API

Create a new lead.

POST /api/leads

Example request

```
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "555-111-2222"
}
```

---

Change lead status.

PATCH /api/leads/{id}/status

Example request

```
{
  "status": "QUALIFIED"
}
```

If the status becomes **QUALIFIED**, the application publishes a `LeadQualifiedEvent` to Kafka.

---

# Failed Events API

Retrieve all failed events.

GET /api/failed-events

Retrieve a single failed event.

GET /api/failed-events/{id}

Reprocess a failed event.

POST /api/failed-events/{id}/reprocess

This endpoint retries processing of a previously failed event.

---

# Failed Event Statistics

Get failed event counts grouped by status.

GET /api/failed-events/stats/by-status

Get failed event counts grouped by event type.

GET /api/failed-events/stats/by-event-type

---

# Running Locally

### Requirements

* Java 17
* Docker
* Maven

---

### Start Kafka

```
docker-compose up
```

---

### Run Application

```
./mvnw spring-boot:run
```

---

# Testing

Testing stack includes:

* JUnit
* Spring Boot Test
* Testcontainers

Integration tests verify:

* event publishing
* Kafka consumption
* database updates

---

# Future Improvements

Potential enhancements:

* Transactional Outbox Pattern
* Distributed tracing
* Observability with Micrometer and Prometheus
* Retry topics with backoff
* Event schema versioning

---

# Author

Barış Ege Acar

Backend developer focusing on **Java, Spring Boot and event-driven architectures**.
