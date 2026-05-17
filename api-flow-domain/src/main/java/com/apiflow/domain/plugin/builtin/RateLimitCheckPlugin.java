package com.apiflow.domain.plugin.builtin;

import com.apiflow.domain.plugin.Plugin;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.plugin.PluginResult;

public class RateLimitCheckPlugin implements Plugin {

    @Override
    public String getCode() {
        return "RATE_LIMIT_CHECK";
    }

    @Override
    public String getName() {
        return "限流检查插件";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public PluginResult execute(PluginContext context) {
        return PluginResult.success();
    }

    @Override
    public PluginResult compensate(PluginContext context) {
        return PluginResult.success();
    }
}
