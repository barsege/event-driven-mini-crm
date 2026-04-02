package com.begac.minicrm.shared.events.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.begac.minicrm.shared.events.FailedEventQueryService;
import com.begac.minicrm.shared.events.FailedEventReprocessService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/failed-events")
public class FailedEventController {
	private final FailedEventQueryService failedEventQueryService;
	private final FailedEventReprocessService failedEventReprocessService;

	public FailedEventController(FailedEventQueryService failedEventQueryService,
			FailedEventReprocessService eventReprocessService) {
		this.failedEventQueryService = failedEventQueryService;
		this.failedEventReprocessService = eventReprocessService;
	}
	
	@GetMapping
	public List<FailedEventResponse> getFailedEvents() {
		return failedEventQueryService.findAll();
	}
	
	@GetMapping("/{id}")
	public FailedEventDetailResponse getFailedEventById(@PathVariable UUID id) {
		return failedEventQueryService.findById(id);
	}
	
	@PostMapping("/{id}/reprocess")
	public void reprocessFailedEvent (@PathVariable UUID id) {
		failedEventReprocessService.reprocess(id);
	}
	
	
}
