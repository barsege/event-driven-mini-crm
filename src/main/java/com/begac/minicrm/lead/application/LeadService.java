package com.begac.minicrm.lead.application;

import com.begac.minicrm.lead.api.ChangeLeadStatusRequest;
import com.begac.minicrm.lead.api.CreateLeadRequest;
import com.begac.minicrm.lead.api.LeadResponse;
import com.begac.minicrm.lead.domain.Lead;
import com.begac.minicrm.lead.domain.LeadStatus;
import com.begac.minicrm.lead.events.LeadEventPublisher;
import com.begac.minicrm.lead.events.LeadEventTypes;
import com.begac.minicrm.lead.events.LeadQualifiedPayload;
import com.begac.minicrm.lead.persistence.LeadRepository;
import com.begac.minicrm.shared.events.EventEnvelope;
import com.begac.minicrm.shared.observability.CorrelationIds;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final LeadEventPublisher leadEventPublisher;

    public LeadService(LeadRepository leadRepository, LeadEventPublisher leadEventPublisher) {
        this.leadRepository = leadRepository;
        this.leadEventPublisher = leadEventPublisher;
    }

    @Transactional
    public LeadResponse create(CreateLeadRequest req) {
        Lead lead = new Lead(req.firstName(), req.lastName(), req.email(), req.phone());
        Lead saved = leadRepository.save(lead);
        return toResponse(saved);
    }

    @Transactional
    public LeadResponse changeStatus(UUID id, ChangeLeadStatusRequest req) {
        var lead = leadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException(id));

        LeadStatus before = lead.getStatus();

        try {
            lead.changeStatus(req.to());
        } catch (IllegalArgumentException e) {
            throw new InvalidLeadStatusTransitionException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new InvalidLeadStatusTransitionException(e.getMessage());
        }

        var saved = leadRepository.save(lead);

        if (before != LeadStatus.QUALIFIED && saved.getStatus() == LeadStatus.QUALIFIED) {
            var payload = new LeadQualifiedPayload(saved.getId());
            var envelope = EventEnvelope.of(
                    LeadEventTypes.LEAD_QUALIFIED,
                    CorrelationIds.current(),
                    payload
            );
            leadEventPublisher.publishLeadQualified(envelope);
        }

        return toResponse(saved);
    }

    private static LeadResponse toResponse(Lead saved) {
        return new LeadResponse(
                saved.getId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt(),
                saved.getVersion()
        );
    }
}