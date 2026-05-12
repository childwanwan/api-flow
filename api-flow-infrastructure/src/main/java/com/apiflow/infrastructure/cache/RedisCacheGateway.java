package com.apiflow.infrastructure.cache;

import com.apiflow.api.cache.CacheGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisCacheGateway implements CacheGateway {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, String.valueOf(value), expireTime, timeUnit);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public boolean expire(String key, long expireTime, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, expireTime, timeUnit));
    }

    @Override
    public Boolean setIfAbsent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(value), expireTime, timeUnit);
    }
}
