package com.begac.minicrm.shared.events;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.begac.minicrm.shared.events.api.FailedEventDetailResponse;
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
						failedEvent.getFailedAt(),
						failedEvent.getStatus(),
						failedEvent.getReprocessedAt(),
						failedEvent.getRetryCount()
				))
				.toList();
	}
	
	public FailedEventDetailResponse findById(UUID id){
		FailedEvent failedEvent = failedEventRepository.findById(id)
				.orElseThrow (()-> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Failed Event Not Found: " +id
						));
		return new FailedEventDetailResponse(
				failedEvent.getId(), 
				failedEvent.getEventId(),
				failedEvent.getEventType(),
				failedEvent.getCorrelationId(),
				failedEvent.getPayload(),
				failedEvent.getFailedAt(),
				failedEvent.getStatus(),
				failedEvent.getReprocessedAt(),
				failedEvent.getRetryCount()
				);
	}
}
