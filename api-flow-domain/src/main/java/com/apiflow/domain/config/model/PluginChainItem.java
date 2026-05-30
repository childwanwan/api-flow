package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class PluginChainItem {

    private final String pluginCode;
    private final Integer order;
    private final Boolean enabled;
    private final String config;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginChainItem other)) return false;
        return ObjectUtil.equal(pluginCode, other.pluginCode)
                && ObjectUtil.equal(order, other.order)
                && ObjectUtil.equal(enabled, other.enabled)
                && ObjectUtil.equal(config, other.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginCode, order, enabled, config);
    }
}
