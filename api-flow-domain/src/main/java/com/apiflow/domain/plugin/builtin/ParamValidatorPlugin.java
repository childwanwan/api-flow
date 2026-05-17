package com.apiflow.domain.plugin.builtin;

import com.apiflow.domain.plugin.Plugin;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.plugin.PluginResult;

public class ParamValidatorPlugin implements Plugin {

    @Override
    public String getCode() {
        return "PARAM_VALIDATOR";
    }

    @Override
    public String getName() {
        return "参数校验插件";
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public PluginResult execute(PluginContext context) {
        if (context.getParams() == null || context.getParams().isEmpty()) {
            return PluginResult.fail("10003", "请求参数不能为空");
        }
        return PluginResult.success();
    }

    @Override
    public PluginResult compensate(PluginContext context) {
        return PluginResult.success();
    }
}
