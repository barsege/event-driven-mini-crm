package com.begac.minicrm.shared.events;

import com.begac.minicrm.shared.events.api.FailedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class FailedEventReprocessService {

    private static final Logger log = LoggerFactory.getLogger(FailedEventReprocessService.class);

    private final FailedEventRepository failedEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public FailedEventReprocessService(
            FailedEventRepository failedEventRepository,
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper
    ) {
        this.failedEventRepository = failedEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void reprocess(UUID id) {
        FailedEvent failedEvent = failedEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Failed Event Not Found: " + id
                ));

        String normalizedPayload = unwrapJsonString(failedEvent.getPayload());

        log.info(
                "Reprocessing failed event. id={}, eventId={}, eventType={}",
                failedEvent.getId(),
                failedEvent.getEventId(),
                failedEvent.getEventType()
        );

        kafkaTemplate.send("crm.lead-events", normalizedPayload);
        
        failedEvent.markReprocessed();
        failedEventRepository.save(failedEvent);
        
        log.info("Normalized payload before reprocess={}", normalizedPayload);

        log.info(
                "Failed event republished to Kafka. id={}, eventId={}, topic={}",
                failedEvent.getId(),
                failedEvent.getEventId(),
                "crm.lead-events"
        );
    }

    private String unwrapJsonString(String payload) {
        String result = payload;

        try {
            while (result.startsWith("\"")) {
                result = objectMapper.readValue(result, String.class);
            }
            return result;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to normalize failed event payload", ex);
        }
    }
}