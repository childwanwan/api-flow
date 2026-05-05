package com.flow.api.cache;

public interface CacheService {

    void set(String key, Object value, long expireSeconds);

    Object get(String key);
}
