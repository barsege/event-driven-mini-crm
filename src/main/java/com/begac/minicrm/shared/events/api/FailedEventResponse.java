package com.begac.minicrm.shared.events.api;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.begac.minicrm.shared.events.FailedEventStatus;

public record FailedEventResponse(
		UUID id,
        UUID eventId,
        String eventType,
        String correlationId,
        OffsetDateTime failedAt,
        FailedEventStatus status,
        OffsetDateTime reprocessedAt,
        int retryCount
) {
}