package com.apiflow.domain.lock;

import com.apiflow.api.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLock {

    private static final String LOCK_PREFIX = "task_lock:";

    private final CacheService cacheService;

    public boolean tryLock(String key, long expireSeconds) {
        String lockKey = LOCK_PREFIX + key;
        try {
            Object existing = cacheService.get(lockKey);
            if (existing != null) {
                return false;
            }
            cacheService.set(lockKey, "locked", expireSeconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("Failed to acquire lock: {}", lockKey, e);
            return false;
        }
    }

    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        try {
            cacheService.delete(lockKey);
        } catch (Exception e) {
            log.error("Failed to release lock: {}", lockKey, e);
        }
    }

    public boolean isLocked(String key) {
        String lockKey = LOCK_PREFIX + key;
        return cacheService.hasKey(lockKey);
    }
}
