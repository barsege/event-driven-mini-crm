package com.begac.minicrm.shared.events.api;

import com.begac.minicrm.shared.events.FailedEventStatus;

public interface FailedEventStatusCountView {

	FailedEventStatus getStatus();

    long getCount();
}
