package com.apiflow.api.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface DistributedLockGateway {

    <T> T executeWithLock(String lockKey, long expireSeconds, TimeUnit unit, Supplier<T> action);

    boolean tryLock(String lockKey, long expireSeconds, TimeUnit unit);

    void unlock(String lockKey);

    boolean isLocked(String lockKey);
}
