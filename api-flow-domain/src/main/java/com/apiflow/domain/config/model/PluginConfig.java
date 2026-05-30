package com.apiflow.domain.config.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class PluginConfig {

    private final Boolean enabled;
    private final List<PluginChainItem> pluginChain;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluginConfig other)) return false;
        return ObjectUtil.equal(enabled, other.enabled) && ObjectUtil.equal(pluginChain, other.pluginChain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, pluginChain);
    }
}
