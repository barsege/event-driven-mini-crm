package com.begac.minicrm.lead.application;

import java.util.UUID;

public class LeadNotFoundException extends RuntimeException {

    public LeadNotFoundException(UUID leadId) {
        super("Lead not found: " + leadId);
    }
}