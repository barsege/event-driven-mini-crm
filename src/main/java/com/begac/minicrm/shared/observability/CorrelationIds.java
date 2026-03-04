package com.begac.minicrm.shared.observability;

import org.slf4j.MDC;

public final class CorrelationIds {
    private static final String MDC_KEY = "correlationId";

    private CorrelationIds() {}

    public static String current() {
        String v = MDC.get(MDC_KEY);
        return (v == null || v.isBlank()) ? "N/A" : v;
    }
}