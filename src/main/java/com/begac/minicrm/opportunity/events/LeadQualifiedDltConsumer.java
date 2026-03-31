package com.begac.minicrm.opportunity.events;

import com.begac.minicrm.shared.events.EventEnvelope;
import com.begac.minicrm.shared.events.FailedEvent;
import com.begac.minicrm.shared.events.api.FailedEventRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LeadQualifiedDltConsumer {

    private static final Logger log = LoggerFactory.getLogger(LeadQualifiedDltConsumer.class);

    private final ObjectMapper objectMapper;
    private final FailedEventRepository failedEventRepository;

    public LeadQualifiedDltConsumer(
            ObjectMapper objectMapper,
            FailedEventRepository failedEventRepository
    ) {
        this.objectMapper = objectMapper;
        this.failedEventRepository = failedEventRepository;
    }

    @KafkaListener(
            topics = "crm.lead-events-dlt",
            groupId = "opportunity-service-dlt",
            containerFactory = "crmKafkaListenerContainerFactory"
    )
    public void consume(String message) throws Exception {
        log.error("Message received from DLT. topic=crm.lead-events-dlt, payload={}", message);

        String rawEventJson = objectMapper.readValue(message, String.class);

        EventEnvelope<Map<String, Object>> event =
                objectMapper.readValue(
                        rawEventJson,
                        new TypeReference<EventEnvelope<Map<String, Object>>>() {}
                );

        FailedEvent failedEvent = new FailedEvent(
                event.eventId(),
                event.eventType(),
                event.correlationId(),
                rawEventJson
        );

        failedEventRepository.save(failedEvent);

        log.error(
                "Failed event saved to database. eventId={}, eventType={}, correlationId={}",
                event.eventId(),
                event.eventType(),
                event.correlationId()
        );
    }
}