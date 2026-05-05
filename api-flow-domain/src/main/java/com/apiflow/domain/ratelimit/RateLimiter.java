package com.apiflow.domain.ratelimit;

import com.apiflow.api.cache.CacheService;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.domain.config.model.RateLimitConfig;
import com.apiflow.domain.config.model.RateLimitRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimiter {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";
    private static final String CONCURRENCY_KEY_PREFIX = "concurrency:";

    private final CacheService cacheService;

    public void checkRateLimit(RateLimitConfig config, String apiCode, Map<String, String> contextVars) {
        if (config == null || !Boolean.TRUE.equals(config.getEnabled())) {
            return;
        }
        if (config.getRules() == null || config.getRules().isEmpty()) {
            return;
        }

        for (RateLimitRule rule : config.getRules()) {
            String key = buildRateLimitKey(rule, apiCode, contextVars);
            if ("QPS".equals(rule.getType())) {
                checkQpsLimit(key, rule);
            } else if ("CONCURRENCY".equals(rule.getType())) {
                checkConcurrencyLimit(key, rule);
            }
        }
    }

    private void checkQpsLimit(String key, RateLimitRule rule) {
        String redisKey = RATE_LIMIT_KEY_PREFIX + key;
        Object currentObj = cacheService.get(redisKey);
        long current = 0;
        if (currentObj != null) {
            try {
                current = Long.parseLong(currentObj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        if (current >= rule.getLimit()) {
            log.warn("QPS rate limit triggered: key={}, current={}, limit={}", key, current, rule.getLimit());
            throw new BusinessException(ErrorCode.RATE_LIMIT_TRIGGERED,
                    "QPS限流触发, key=" + key + ", 当前QPS=" + current + ", 限制=" + rule.getLimit());
        }

        cacheService.set(redisKey, String.valueOf(current + 1), rule.getWindowSeconds(), TimeUnit.SECONDS);
    }

    private void checkConcurrencyLimit(String key, RateLimitRule rule) {
        String redisKey = CONCURRENCY_KEY_PREFIX + key;
        Object currentObj = cacheService.get(redisKey);
        long current = 0;
        if (currentObj != null) {
            try {
                current = Long.parseLong(currentObj.toString());
            } catch (NumberFormatException ignored) {
            }
        }

        if (current >= rule.getLimit()) {
            log.warn("Concurrency limit triggered: key={}, current={}, limit={}", key, current, rule.getLimit());
            throw new BusinessException(ErrorCode.RATE_LIMIT_TRIGGERED,
                    "并发限流触发, key=" + key + ", 当前并发=" + current + ", 限制=" + rule.getLimit());
        }

        cacheService.set(redisKey, String.valueOf(current + 1), 300, TimeUnit.SECONDS);
    }

    public void releaseConcurrency(String apiCode, RateLimitConfig config, Map<String, String> contextVars) {
        if (config == null || !Boolean.TRUE.equals(config.getEnabled()) || config.getRules() == null) {
            return;
        }
        for (RateLimitRule rule : config.getRules()) {
            if ("CONCURRENCY".equals(rule.getType())) {
                String key = buildRateLimitKey(rule, apiCode, contextVars);
                String redisKey = CONCURRENCY_KEY_PREFIX + key;
                Object currentObj = cacheService.get(redisKey);
                if (currentObj != null) {
                    try {
                        long current = Long.parseLong(currentObj.toString());
                        if (current > 0) {
                            cacheService.set(redisKey, String.valueOf(current - 1), 300, TimeUnit.SECONDS);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
    }

    private String buildRateLimitKey(RateLimitRule rule, String apiCode, Map<String, String> contextVars) {
        String keyTemplate = rule.getKeyTemplate();
        if (keyTemplate == null || keyTemplate.isEmpty()) {
            return apiCode + ":" + rule.getName();
        }
        String key = keyTemplate;
        if (contextVars != null) {
            for (Map.Entry<String, String> entry : contextVars.entrySet()) {
                key = key.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return key;
    }
}
