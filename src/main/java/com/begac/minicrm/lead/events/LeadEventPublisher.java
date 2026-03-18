package com.begac.minicrm.lead.events;

import com.begac.minicrm.shared.events.EventEnvelope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class LeadEventPublisher {

    public static final String TOPIC = "crm.lead-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private static final Logger log = LoggerFactory.getLogger(LeadEventPublisher.class);

    public LeadEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishLeadQualified(EventEnvelope<LeadQualifiedPayload> event) {
        // key: leadId -> ordering için iyi (aynı lead eventleri aynı partition’a düşer)
        kafkaTemplate.send(TOPIC, event.payload().leadId().toString(), event);
        
        log.info(
                "Publishing event. topic={}, eventType={}, eventId={}, correlationId={}",
                TOPIC,
                event.eventType(),
                event.eventId(),
                event.correlationId()
        );
    }
}