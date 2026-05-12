package com.apiflow.common.util;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public final class TraceContext {

    public static final String TRACE_ID = "traceId";

    private TraceContext() {
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void setTraceId(String traceId) {
        if (traceId != null) {
            MDC.put(TRACE_ID, traceId);
        }
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
    }

    public static Map<String, String> getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    public static void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null) {
            MDC.setContextMap(contextMap);
        }
    }

    public static Runnable wrap(Runnable task) {
        Map<String, String> contextMap = getCopyOfContextMap();
        return () -> {
            setContextMap(contextMap);
            try {
                task.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static <T> Supplier<T> wrap(Supplier<T> supplier) {
        Map<String, String> contextMap = getCopyOfContextMap();
        return () -> {
            setContextMap(contextMap);
            try {
                return supplier.get();
            } finally {
                MDC.clear();
            }
        };
    }
}
