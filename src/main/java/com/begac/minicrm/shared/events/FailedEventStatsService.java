package com.begac.minicrm.shared.events;

import java.util.List;

import org.springframework.stereotype.Service;

import com.begac.minicrm.shared.events.api.FailedEventRepository;
import com.begac.minicrm.shared.events.api.FailedEventStatusStatsResponse;
import com.begac.minicrm.shared.events.api.FailedEventTypeStatsResponse;

@Service
public class FailedEventStatsService {

	private final FailedEventRepository failedEventRepository;

    public FailedEventStatsService(FailedEventRepository failedEventRepository) {
        this.failedEventRepository = failedEventRepository;
    }

    public List<FailedEventStatusStatsResponse> getCountByStatus() {
        return failedEventRepository.countByStatus()
                .stream()
                .map(row -> new FailedEventStatusStatsResponse(
                        row.getStatus(),
                        row.getCount()
                ))
                .toList();
    }
    
    public List<FailedEventTypeStatsResponse> getCountByEventType() {
        return failedEventRepository.countByEventType()
                .stream()
                .map(row -> new FailedEventTypeStatsResponse(
                        row.getEventType(),
                        row.getCount()
                ))
                .toList();
    }
}
