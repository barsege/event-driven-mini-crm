	package com.begac.minicrm.opportunity.events;
	
	import com.begac.minicrm.lead.events.LeadEventTypes;
	import com.begac.minicrm.lead.events.LeadQualifiedPayload;
	import com.begac.minicrm.opportunity.application.OpportunityService;
	import com.begac.minicrm.shared.events.EventEnvelope;
	import com.fasterxml.jackson.core.type.TypeReference;
	import com.fasterxml.jackson.databind.ObjectMapper;
	
	import java.util.UUID;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.slf4j.MDC;
	import org.springframework.kafka.annotation.KafkaListener;
	import org.springframework.stereotype.Component;
	
	@Component
	public class LeadQualifiedConsumer {
	
	    private final OpportunityService opportunityService;
	    private final ObjectMapper objectMapper;
	
	    private static final Logger log = LoggerFactory.getLogger(LeadQualifiedConsumer.class);
	
	    public LeadQualifiedConsumer(
	            OpportunityService opportunityService,
	            ObjectMapper objectMapper
	    ) {
	        this.opportunityService = opportunityService;
	        this.objectMapper = objectMapper;
	    }
	
	    @KafkaListener(
	            topics = "crm.lead-events",
	            groupId = "opportunity-service",
	            containerFactory = "crmKafkaListenerContainerFactory"
	    )
	    public void consume(String message) throws Exception {
	
	        EventEnvelope<LeadQualifiedPayload> event =
	                objectMapper.readValue(
	                        message,
	                        new TypeReference<EventEnvelope<LeadQualifiedPayload>>() {}
	                );
	        
	
	        MDC.put("correlationId", event.correlationId());
	        
	        if(true)
	        	throw new IllegalArgumentException("Permanent test failure");
	
	        try {
	            log.info(
	                    "Received message from Kafka. topic=crm.lead-events, eventType={}, eventId={}, correlationId={}",
	                    event.eventType(),
	                    event.eventId(),
	                    event.correlationId()
	            );
	
	            if (!LeadEventTypes.LEAD_QUALIFIED.equals(event.eventType())) {
	                log.info(
	                        "Ignoring unsupported event type. eventType={}, eventId={}",
	                        event.eventType(),
	                        event.eventId()
	                );
	                return;
	            }
	
	            opportunityService.handleLeadQualified(event);
	
	            log.info(
	                    "Successfully consumed event. eventType={}, eventId={}, correlationId={}",
	                    event.eventType(),
	                    event.eventId(),
	                    event.correlationId()
	            );
	        } finally {
	            MDC.remove("correlationId");
	        }
	    }
	}