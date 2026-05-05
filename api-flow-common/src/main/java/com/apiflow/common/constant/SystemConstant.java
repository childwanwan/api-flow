package com.apiflow.common.constant;

public class SystemConstant {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int MAX_QUERY_LIMIT = 1000;
    public static final int DEFAULT_BATCH_SIZE = 100;

    public static final Long DEFAULT_REQUEST_TIMEOUT_MS = 30000L;
    public static final int DEFAULT_AUTO_RETRY_COUNT = 64;
    public static final Long DEFAULT_RETRY_INTERVAL_MS = 5000L;
    public static final int DEFAULT_MAX_QUEUE_SIZE = 100000;
    public static final int DEFAULT_TASK_LOCK_EXPIRE_SECONDS = 300;
    public static final int DEFAULT_COMPENSATE_WAIT_SECONDS = 30;
    public static final int DEFAULT_FORCE_INTERRUPT_SECONDS = 60;
    public static final int DEFAULT_RUNNING_TIMEOUT_MINUTES = 30;
    public static final int DEFAULT_COMPENSATE_RETRY_MAX_COUNT = 64;
    public static final Long DEFAULT_COMPENSATE_RETRY_BASE_INTERVAL_MS = 1000L;
    public static final int DEFAULT_RECEIPT_RETRY_MAX_COUNT = 64;
    public static final Long DEFAULT_RECEIPT_RETRY_BASE_INTERVAL_MS = 1000L;

    private SystemConstant() {
    }

}
