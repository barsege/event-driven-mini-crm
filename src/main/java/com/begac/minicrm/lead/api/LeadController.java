package com.begac.minicrm.lead.api;

import com.begac.minicrm.lead.application.LeadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse create(@RequestBody CreateLeadRequest req) {
        return leadService.create(req);
    }
}
