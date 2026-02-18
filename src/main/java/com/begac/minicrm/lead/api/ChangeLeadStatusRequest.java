package com.begac.minicrm.lead.api;

import com.begac.minicrm.lead.domain.LeadStatus;

public record ChangeLeadStatusRequest(LeadStatus to) {}
