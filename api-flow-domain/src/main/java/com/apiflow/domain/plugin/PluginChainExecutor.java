package com.apiflow.domain.plugin;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.domain.task.model.ExecInfo;
import com.apiflow.domain.task.model.PluginStep;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PluginChainExecutor {

    private final Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    public void registerPlugin(Plugin plugin) {
        pluginMap.put(plugin.getCode(), plugin);
    }

    public void unregisterPlugin(String pluginCode) {
        pluginMap.remove(pluginCode);
    }

    public Plugin getPlugin(String pluginCode) {
        return pluginMap.get(pluginCode);
    }

    public ExecInfo execute(PluginContext context, List<PluginChainItemConfig> chainItems) {
        List<PluginStep> steps = new ArrayList<>();
        long totalStartTime = System.currentTimeMillis();

        List<PluginChainItemConfig> sortedItems = chainItems.stream()
                .filter(PluginChainItemConfig::getEnabled)
                .sorted(Comparator.comparingInt(PluginChainItemConfig::getOrder))
                .toList();

        for (PluginChainItemConfig item : sortedItems) {
            if (context.isInterrupted()) {
                log.warn("Plugin chain interrupted before executing plugin: {}", item.getPluginCode());
                break;
            }

            Plugin plugin = pluginMap.get(item.getPluginCode());
            if (plugin == null) {
                throw new BusinessException(ErrorCode.PLUGIN_NOT_FOUND, "Plugin not found: " + item.getPluginCode());
            }

            long stepStartTime = System.currentTimeMillis();
            PluginStep step = PluginStep.builder()
                    .stepOrder(item.getOrder())
                    .pluginCode(plugin.getCode())
                    .pluginName(plugin.getName())
                    .status("RUNNING")
                    .inputData(context.getParams())
                    .executeStartTimeMs(stepStartTime)
                    .build();
            steps.add(step);

            try {
                PluginResult result = plugin.execute(context);
                long stepEndTime = System.currentTimeMillis();
                step.setStatus(result.isSuccess() ? "SUCCESS" : "FAILED");
                step.setOutputData(result.getData());
                step.setExecuteEndTimeMs(stepEndTime);
                step.setExecuteCostTimeMs(stepEndTime - stepStartTime);
                step.setCompensateData(context.getCompensateData());

                if (!result.isSuccess()) {
                    throw new BusinessException(ErrorCode.PLUGIN_EXECUTE_FAILED, result.getMessage());
                }
            } catch (BusinessException e) {
                long stepEndTime = System.currentTimeMillis();
                step.setStatus("FAILED");
                step.setExecuteEndTimeMs(stepEndTime);
                step.setExecuteCostTimeMs(stepEndTime - stepStartTime);
                step.setCompensateData(context.getCompensateData());
                throw e;
            } catch (Exception e) {
                long stepEndTime = System.currentTimeMillis();
                step.setStatus("FAILED");
                step.setExecuteEndTimeMs(stepEndTime);
                step.setExecuteCostTimeMs(stepEndTime - stepStartTime);
                step.setCompensateData(context.getCompensateData());
                throw new BusinessException(ErrorCode.PLUGIN_EXECUTE_FAILED, e.getMessage());
            }
        }

        long totalEndTime = System.currentTimeMillis();
        return ExecInfo.builder()
                .steps(steps)
                .totalCostTimeMs(totalEndTime - totalStartTime)
                .build();
    }

    public void compensate(PluginContext context, List<PluginStep> completedSteps) {
        List<PluginStep> reversedSteps = completedSteps.stream()
                .filter(s -> "SUCCESS".equals(s.getStatus()))
                .sorted((a, b) -> Integer.compare(b.getStepOrder(), a.getStepOrder()))
                .toList();

        for (PluginStep step : reversedSteps) {
            Plugin plugin = pluginMap.get(step.getPluginCode());
            if (plugin == null) {
                log.warn("Plugin not found for compensation: {}", step.getPluginCode());
                continue;
            }
            try {
                PluginResult result = plugin.compensate(context);
                if (!result.isSuccess()) {
                    log.error("Plugin compensation failed: {}, reason: {}", step.getPluginCode(), result.getMessage());
                    throw new BusinessException(ErrorCode.COMPENSATE_FAILED, result.getMessage());
                }
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                log.error("Plugin compensation error: {}", step.getPluginCode(), e);
                throw new BusinessException(ErrorCode.COMPENSATE_FAILED, e.getMessage());
            }
        }
    }

    public static class PluginChainItemConfig {
        private final String pluginCode;
        private final int order;
        private final boolean enabled;
        private final String config;

        public PluginChainItemConfig(String pluginCode, int order, boolean enabled, String config) {
            this.pluginCode = pluginCode;
            this.order = order;
            this.enabled = enabled;
            this.config = config;
        }

        public String getPluginCode() { return pluginCode; }
        public int getOrder() { return order; }
        public boolean getEnabled() { return enabled; }
        public String getConfig() { return config; }
    }
}
