package com.begac.minicrm.shared.events.api;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FailedEventResponse(
        UUID id,
        UUID eventId,
        String eventType,
        String correlationId,
        OffsetDateTime failedAt
) {
}