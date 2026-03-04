package com.begac.minicrm.lead.events;

import java.util.UUID;

public record LeadQualifiedPayload(
        UUID leadId
) {}