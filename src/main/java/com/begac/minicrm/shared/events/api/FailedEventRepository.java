package com.begac.minicrm.shared.events.api;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.begac.minicrm.shared.events.FailedEvent;
import com.begac.minicrm.shared.events.FailedEventStatus;

public interface FailedEventRepository extends JpaRepository<FailedEvent, UUID> {
	List<FailedEvent> findAllByOrderByFailedAtDesc();

    List<FailedEvent> findByStatusOrderByFailedAtDesc(FailedEventStatus status);

    @Query("""
        select f.status as status, count(f) as count
        from FailedEvent f
        group by f.status
    """)
    List<FailedEventStatusCountView> countByStatus();
    
    @Query("""
    	 select f.eventType as eventType, count(f) as count
    	 from FailedEvent f
    	 group by f.eventType
    	 order by count(f) desc
    """)
    List<FailedEventTypeCountView> countByEventType();
	

}
