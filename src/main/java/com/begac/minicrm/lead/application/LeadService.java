package com.begac.minicrm.lead.application;

import com.begac.minicrm.lead.api.CreateLeadRequest;
import com.begac.minicrm.lead.api.LeadResponse;
import com.begac.minicrm.lead.domain.Lead;
import com.begac.minicrm.lead.persistence.LeadRepository;
import com.begac.minicrm.lead.api.ChangeLeadStatusRequest;	
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    @Transactional
    public LeadResponse create(CreateLeadRequest req) {
        Lead lead = new Lead(req.firstName(), req.lastName(), req.email(), req.phone());
        Lead saved = leadRepository.save(lead);

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
    
    @Transactional
    public LeadResponse changeStatus(UUID id, ChangeLeadStatusRequest req) {
        var lead = leadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lead not found: " + id));

        try {
            lead.changeStatus(req.to()); // kural kontrolü entity içinde
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // kural ihlali: state transition yasak
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }

        var saved = leadRepository.save(lead);

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
