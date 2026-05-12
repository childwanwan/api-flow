package com.apiflow.application;

import com.apiflow.api.cache.DistributedLockGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockHelper {

    private static final long DEFAULT_EXPIRE_SECONDS = 30;
    private static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    private final DistributedLockGateway distributedLockGateway;

    public <T> T execute(String keyPrefix, String keySuffix, Supplier<T> action) {
        return executeWithConfig(keyPrefix, keySuffix, DEFAULT_EXPIRE_SECONDS, DEFAULT_UNIT, action);
    }

    public <T> T executeWithConfig(String keyPrefix, String keySuffix,
                                    long expireSeconds, TimeUnit unit,
                                    Supplier<T> action) {
        String lockKey = buildLockKey(keyPrefix, keySuffix);
        return distributedLockGateway.executeWithLock(lockKey, expireSeconds, unit, action);
    }

    public void executeVoid(String keyPrefix, String keySuffix, Runnable action) {
        execute(keyPrefix, keySuffix, () -> {
            action.run();
            return null;
        });
    }

    public void executeVoidWithConfig(String keyPrefix, String keySuffix,
                                      long expireSeconds, TimeUnit unit,
                                      Runnable action) {
        executeWithConfig(keyPrefix, keySuffix, expireSeconds, unit, () -> {
            action.run();
            return null;
        });
    }

    private String buildLockKey(String keyPrefix, String keySuffix) {
        return keyPrefix + ":" + keySuffix;
    }
}
