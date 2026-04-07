package com.begac.minicrm.shared.events;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
		name = "failed_events",
				indexes = {
				        @Index(name = "idx_failed_events_event_id", columnList = "event_id"),
				        @Index(name = "idx_failed_events_status", columnList = "status"),
				        @Index(name = "idx_failed_events_failed_at", columnList = "failed_at"),
				        @Index(name = "idx_failed_events_status_failed_at", columnList = "status, failed_at")
				}
		)
public class FailedEvent {

    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "failed_at", nullable = false)
    private OffsetDateTime failedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FailedEventStatus status;

    @Column(name = "reprocessed_at")
    private OffsetDateTime reprocessedAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    protected FailedEvent() {
    }

    public FailedEvent(UUID eventId, String eventType, String correlationId, String payload) {
        this.id = UUID.randomUUID();
        this.eventId = eventId;
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.payload = payload;
        this.failedAt = OffsetDateTime.now();
        this.status = FailedEventStatus.NEW;
        this.retryCount = 0;
    }

    public void markReprocessed() {
        this.status = FailedEventStatus.REPROCESSED;
        this.reprocessedAt = OffsetDateTime.now();
        this.retryCount++;
    }

    public void markReprocessFailed() {
        this.status = FailedEventStatus.REPROCESS_FAILED;
        this.retryCount++;
    }

    public UUID getId() {
        return id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getPayload() {
        return payload;
    }

    public OffsetDateTime getFailedAt() {
        return failedAt;
    }

    public FailedEventStatus getStatus() {
        return status;
    }

    public OffsetDateTime getReprocessedAt() {
        return reprocessedAt;
    }

    public int getRetryCount() {
        return retryCount;
    }
}