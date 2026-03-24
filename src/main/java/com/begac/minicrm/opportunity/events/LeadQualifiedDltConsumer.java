package com.begac.minicrm.opportunity.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LeadQualifiedDltConsumer {

	private static final Logger log = LoggerFactory.getLogger(LeadQualifiedDltConsumer.class);
	
	@KafkaListener(
			topics = "crm.lead-events-dlt",
			groupId = "crm.lead-events-dlt",
			containerFactory = "crmKafkaListenerContainerFactory"
	)
	public void consume (String message) {
		log.error("Message received from DLT. topic=crm.lead-events-dlt, payload={}", message);
	}
	
}
