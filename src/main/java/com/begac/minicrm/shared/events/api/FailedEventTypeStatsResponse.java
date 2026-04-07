package com.begac.minicrm.shared.events.api;

public record FailedEventTypeStatsResponse(
        String eventType,
        long count
) {
}