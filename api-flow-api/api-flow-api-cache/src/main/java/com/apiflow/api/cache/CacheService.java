package com.apiflow.api.cache;

import java.util.concurrent.TimeUnit;

public interface CacheService {

    void set(String key, Object value, long expireTime, TimeUnit timeUnit);

    Object get(String key);

    void delete(String key);

    boolean hasKey(String key);

    boolean expire(String key, long expireTime, TimeUnit timeUnit);
}
