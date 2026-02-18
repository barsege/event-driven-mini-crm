package com.begac.minicrm.lead.application;

import com.begac.minicrm.lead.api.CreateLeadRequest;
import com.begac.minicrm.lead.api.LeadResponse;
import com.begac.minicrm.lead.domain.Lead;
import com.begac.minicrm.lead.persistence.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
