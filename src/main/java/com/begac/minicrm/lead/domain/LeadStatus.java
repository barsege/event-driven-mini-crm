package com.begac.minicrm.lead.domain;

import java.util.EnumSet;
import java.util.Set;

public enum LeadStatus {

    NEW,
    WORKING,
    QUALIFIED,
    DISQUALIFIED;

    private Set<LeadStatus> allowedNextStatuses;

    static {
        NEW.allowedNextStatuses = EnumSet.of(WORKING, DISQUALIFIED);
        WORKING.allowedNextStatuses = EnumSet.of(QUALIFIED, DISQUALIFIED);
        QUALIFIED.allowedNextStatuses = EnumSet.noneOf(LeadStatus.class);
        DISQUALIFIED.allowedNextStatuses = EnumSet.noneOf(LeadStatus.class);
    }

    public boolean canTransitionTo(LeadStatus targetStatus) {
        return allowedNextStatuses.contains(targetStatus);
    }

    public boolean isTerminal() {
        return allowedNextStatuses.isEmpty();
    }
}
