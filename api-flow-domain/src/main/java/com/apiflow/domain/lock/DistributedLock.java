package com.apiflow.domain.lock;

import com.apiflow.api.cache.CacheGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
@Deprecated
@RequiredArgsConstructor
public class DistributedLock {

    private static final String LOCK_PREFIX = "task_lock:";

    private final CacheGateway cacheGateway;

    public boolean tryLock(String key, long expireSeconds) {
        String lockKey = LOCK_PREFIX + key;
        try {
            Boolean acquired = cacheGateway.setIfAbsent(lockKey, "locked", expireSeconds, TimeUnit.SECONDS);
            return acquired != null && acquired;
        } catch (Exception e) {
            log.error("Failed to acquire lock: {}", lockKey, e);
            return false;
        }
    }

    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        try {
            cacheGateway.delete(lockKey);
        } catch (Exception e) {
            log.error("Failed to release lock: {}", lockKey, e);
        }
    }

    public boolean isLocked(String key) {
        String lockKey = LOCK_PREFIX + key;
        return cacheGateway.hasKey(lockKey);
    }
}