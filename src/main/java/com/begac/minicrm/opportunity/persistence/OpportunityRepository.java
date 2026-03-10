package com.begac.minicrm.opportunity.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.begac.minicrm.opportunity.domain.Opportunity;

public interface OpportunityRepository extends JpaRepository<Opportunity, UUID> {

	boolean existsByLeadId(UUID leadId);
}
