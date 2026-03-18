package com.begac.minicrm.opportunity.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.begac.minicrm.lead.events.LeadQualifiedPayload;
import com.begac.minicrm.opportunity.domain.Opportunity;
import com.begac.minicrm.opportunity.persistence.OpportunityRepository;
import com.begac.minicrm.shared.events.EventEnvelope;
import com.begac.minicrm.shared.events.ProcessedEvent;
import com.begac.minicrm.shared.events.ProcessedEventRepository;

@Service
public class OpportunityService {
	
	private final OpportunityRepository opportunityRepository;
	private final ProcessedEventRepository processedEventRepository;
	
	private static final Logger log = LoggerFactory.getLogger(OpportunityService.class);
	
	public OpportunityService(OpportunityRepository opportunityRepository,
			ProcessedEventRepository processedEventRepository) {
		this.opportunityRepository = opportunityRepository;
		this.processedEventRepository = processedEventRepository;
	}
	
	@Transactional
	public void handleLeadQualified(EventEnvelope<LeadQualifiedPayload> event) {
		log.info(
		        "Handling LeadQualified event. eventId={}, correlationId={}, leadId={}",
		        event.eventId(),
		        event.correlationId(),
		        event.payload().leadId()
		);
		
		if(processedEventRepository.existsById(event.eventId())) {
			log.info(
			        "Event already processed. Skipping. eventId={}, correlationId={}",
			        event.eventId(),
			        event.correlationId()
			);
			return;
		}
		
		var leadId = event.payload().leadId();
		
		if(!opportunityRepository.existsByLeadId(leadId)) {
			Opportunity opportunity = new Opportunity(leadId);
			opportunityRepository.save(opportunity);
			
			log.info(
			        "Opportunity created for lead. leadId={}, correlationId={}",
			        leadId,
			        event.correlationId()
			);
		} else {
			log.info(
			        "Opportunity already exists for lead. Skipping creation. leadId={}, correlationId={}",
			        leadId,
			        event.correlationId()
			);
		}
		
		processedEventRepository.save(new ProcessedEvent(event.eventId(), event.eventType()));
		
		log.info(
		        "Event marked as processed. eventId={}, correlationId={}",
		        event.eventId(),
		        event.correlationId()
		);	
	}
}
