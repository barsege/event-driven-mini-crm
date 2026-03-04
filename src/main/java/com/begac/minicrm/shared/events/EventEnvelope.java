package com.begac.minicrm.shared.events;

import java.time.Instant;
import java.util.UUID;

public record EventEnvelope<T>(
        UUID eventId,
        String eventType,
        String correlationId,
        Instant occurredAt,
        T payload
) {
    public static <T> EventEnvelope<T> of(String eventType, String correlationId, T payload) {
        return new EventEnvelope<>(
                UUID.randomUUID(),
                eventType,
                correlationId,
                Instant.now(),
                payload
        );
    }
}