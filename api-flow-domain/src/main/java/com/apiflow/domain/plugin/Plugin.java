package com.apiflow.domain.plugin;

import java.util.Map;

public interface Plugin {

    String getCode();

    String getName();

    default int getOrder() {
        return 0;
    }

    PluginResult execute(PluginContext context);

    default PluginResult compensate(PluginContext context) {
        return PluginResult.success();
    }
}
