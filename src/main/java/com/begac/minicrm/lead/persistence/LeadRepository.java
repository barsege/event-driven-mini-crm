package com.begac.minicrm.lead.persistence;

import com.begac.minicrm.lead.domain.Lead;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, UUID> {
}
