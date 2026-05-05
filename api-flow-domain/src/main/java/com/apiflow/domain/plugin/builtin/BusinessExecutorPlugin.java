package com.apiflow.domain.plugin.builtin;

import com.apiflow.domain.plugin.Plugin;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.plugin.PluginResult;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BusinessExecutorPlugin implements Plugin {

    @Override
    public String getCode() {
        return "BUSINESS_EXECUTOR";
    }

    @Override
    public String getName() {
        return "业务执行插件";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public PluginResult execute(PluginContext context) {
        context.putCompensateData("apiCode", context.getApiCode());
        context.putCompensateData("taskNo", context.getTaskNo());
        return PluginResult.success("Business logic executed", Map.of("result", "success"));
    }

    @Override
    public PluginResult compensate(PluginContext context) {
        return PluginResult.success();
    }
}
