package com.begac.minicrm.shared.events.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.begac.minicrm.shared.events.FailedEventQueryService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/failed-events")
public class FailedEventController {
	private final FailedEventQueryService failedEventQueryService;

	public FailedEventController(FailedEventQueryService failedEventQueryService) {
		this.failedEventQueryService = failedEventQueryService;
	}
	
	@GetMapping
	public List<FailedEventResponse> getFailedEvents() {
		return failedEventQueryService.findAll();
	}
	
}
