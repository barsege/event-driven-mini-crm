package com.begac.minicrm.shared.events;

import java.util.List;

import org.springframework.stereotype.Service;

import com.begac.minicrm.shared.events.api.FailedEventRepository;
import com.begac.minicrm.shared.events.api.FailedEventResponse;

@Service
public class FailedEventQueryService {

	private final FailedEventRepository failedEventRepository;

	public FailedEventQueryService(FailedEventRepository failedEventRepository) {
		this.failedEventRepository = failedEventRepository;
	}
	
	public List<FailedEventResponse> findAll(){
		return failedEventRepository.findAllByOrderByFailedAtDesc()
				.stream()
				.map(failedEvent -> new FailedEventResponse(
						failedEvent.getId(), 
						failedEvent.getEventId(), 
						failedEvent.getEventType(), 
						failedEvent.getCorrelationId(), 
						failedEvent.getFailedAt()
				))
				.toList();
	}
}
