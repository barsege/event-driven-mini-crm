package com.begac.minicrm.opportunity.application;

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
	
	public OpportunityService(OpportunityRepository opportunityRepository,
			ProcessedEventRepository processedEventRepository) {
		this.opportunityRepository = opportunityRepository;
		this.processedEventRepository = processedEventRepository;
	}
	
	@Transactional
	public void handleLeadQualified(EventEnvelope<LeadQualifiedPayload> event) {
		if(processedEventRepository.existsById(event.eventId())) {
			return;
		}
		
		var leadId = event.payload().leadId();
		
		if(!opportunityRepository.existsByLeadId(leadId)) {
			Opportunity opportunity = new Opportunity(leadId);
			opportunityRepository.save(opportunity);
		}
		
		processedEventRepository.save(new ProcessedEvent(event.eventId(), event.eventType()));
	}
	

}
