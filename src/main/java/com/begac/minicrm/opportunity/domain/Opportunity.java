package com.begac.minicrm.opportunity.domain;


import java.util.UUID;

import com.begac.minicrm.shared.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "opportunities")
public class Opportunity extends BaseEntity{

	@Column(nullable = false, unique = true)
	private UUID leadId;
	
	protected Opportunity() {
	}
	
	public Opportunity(UUID leadId) {
		this.leadId = leadId;
	}
	
	public UUID getLeadId() {
		return leadId;
	}
}
