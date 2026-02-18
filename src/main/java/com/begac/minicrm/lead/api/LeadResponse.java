package com.begac.minicrm.lead.api;

import com.begac.minicrm.lead.domain.LeadStatus;
import java.time.Instant;
import java.util.UUID;

public record LeadResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LeadStatus status,
        Instant createdAt,
        Instant updatedAt,
        long version
) {}
