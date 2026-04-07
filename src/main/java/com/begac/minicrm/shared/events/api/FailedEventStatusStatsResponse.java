package com.begac.minicrm.shared.events.api;

import com.begac.minicrm.shared.events.FailedEventStatus;

public record FailedEventStatusStatsResponse(
        FailedEventStatus status,
        long count
) {
}
