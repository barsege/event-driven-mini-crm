package com.begac.minicrm.opportunity.events;

import com.begac.minicrm.lead.events.LeadEventTypes;
import com.begac.minicrm.lead.events.LeadQualifiedPayload;
import com.begac.minicrm.opportunity.application.OpportunityService;
import com.begac.minicrm.shared.events.EventEnvelope;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LeadQualifiedConsumer {

    private final OpportunityService opportunityService;
    private final ObjectMapper objectMapper;

    public LeadQualifiedConsumer(
            OpportunityService opportunityService,
            ObjectMapper objectMapper
    ) {
        this.opportunityService = opportunityService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "crm.lead-events",
            groupId = "opportunity-service"
    )
    public void consume(String message) throws Exception {

        EventEnvelope<LeadQualifiedPayload> event =
                objectMapper.readValue(
                        message,
                        new TypeReference<EventEnvelope<LeadQualifiedPayload>>() {}
                );

        if (!LeadEventTypes.LEAD_QUALIFIED.equals(event.eventType())) {
            return;
        }

        opportunityService.handleLeadQualified(event);
    }
}