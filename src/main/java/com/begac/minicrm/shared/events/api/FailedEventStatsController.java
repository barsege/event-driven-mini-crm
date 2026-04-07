package com.begac.minicrm.shared.events.api;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.begac.minicrm.shared.events.FailedEventStatsService;

@RestController
@RequestMapping("/api/failed-events/stats")
public class FailedEventStatsController {

	private final FailedEventStatsService failedEventStatsService;

    public FailedEventStatsController(FailedEventStatsService failedEventStatsService) {
        this.failedEventStatsService = failedEventStatsService;
    }

    @GetMapping("/by-status")
    public List<FailedEventStatusStatsResponse> getByStatus() {
        return failedEventStatsService.getCountByStatus();
    }
    
    @GetMapping("/by-event-type")
    public List<FailedEventTypeStatsResponse> getByEventType() {
        return failedEventStatsService.getCountByEventType();
    }
}