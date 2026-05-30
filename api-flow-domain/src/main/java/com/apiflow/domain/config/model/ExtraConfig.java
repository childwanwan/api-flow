package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class ExtraConfig {

    private final Map<String, String> envConfig;
    private final String targetUrl;
    private final String targetMethod;
    private final Map<String, String> targetHeaders;
    private final String targetBodyTemplate;
    private final Integer targetTimeoutMs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtraConfig other)) return false;
        return ObjectUtil.equal(envConfig, other.envConfig)
                && ObjectUtil.equal(targetUrl, other.targetUrl)
                && ObjectUtil.equal(targetMethod, other.targetMethod)
                && ObjectUtil.equal(targetHeaders, other.targetHeaders)
                && ObjectUtil.equal(targetBodyTemplate, other.targetBodyTemplate)
                && ObjectUtil.equal(targetTimeoutMs, other.targetTimeoutMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(envConfig, targetUrl, targetMethod, targetHeaders, targetBodyTemplate, targetTimeoutMs);
    }
}
