package com.begac.minicrm.shared.events.api;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.begac.minicrm.shared.events.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, UUID> {
	List<FailedEvent> findAllByOrderByFailedAtDesc();
	

}
