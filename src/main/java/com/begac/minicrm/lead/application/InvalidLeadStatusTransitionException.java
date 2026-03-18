package com.begac.minicrm.lead.application;

import com.begac.minicrm.lead.domain.LeadStatus;

public class InvalidLeadStatusTransitionException extends RuntimeException {

    public InvalidLeadStatusTransitionException(String message) {
        super(message);
    }
}