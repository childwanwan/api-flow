package com.apiflow.interfaces.biz.plugin;

import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.application.plugin.PluginConfigApplicationService;
import com.apiflow.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plugin")
public class PluginConfigController {

    private final PluginConfigApplicationService pluginConfigApplicationService;

    @PostMapping("/create")
    public Result<PluginConfigIDTO> createPlugin(@RequestBody Map<String, Object> request) {
        String pluginCode = (String) request.get("pluginCode");
        String pluginName = (String) request.get("pluginName");
        String pluginClass = (String) request.get("pluginClass");
        String description = (String) request.get("description");
        String config = request.get("config") != null ? request.get("config").toString() : null;
        Boolean enabled = request.get("enabled") != null ? (Boolean) request.get("enabled") : true;
        Integer orderNum = request.get("orderNum") != null ? ((Number) request.get("orderNum")).intValue() : 0;
        PluginConfigIDTO plugin = pluginConfigApplicationService.createPlugin(
                pluginCode, pluginName, pluginClass, description, config, enabled, orderNum);
        return Result.success(plugin);
    }

    @PutMapping("/update")
    public Result<PluginConfigIDTO> updatePlugin(@RequestBody Map<String, Object> request) {
        String pluginCode = (String) request.get("pluginCode");
        String pluginName = (String) request.get("pluginName");
        String pluginClass = (String) request.get("pluginClass");
        String description = (String) request.get("description");
        String config = request.get("config") != null ? request.get("config").toString() : null;
        Boolean enabled = request.get("enabled") != null ? (Boolean) request.get("enabled") : null;
        Integer orderNum = request.get("orderNum") != null ? ((Number) request.get("orderNum")).intValue() : null;
        PluginConfigIDTO plugin = pluginConfigApplicationService.updatePlugin(
                pluginCode, pluginName, pluginClass, description, config, enabled, orderNum);
        return Result.success(plugin);
    }

    @GetMapping("/{pluginCode}")
    public Result<PluginConfigIDTO> getPlugin(@PathVariable String pluginCode) {
        PluginConfigIDTO plugin = pluginConfigApplicationService.getPlugin(pluginCode);
        return Result.success(plugin);
    }

    @GetMapping("/list")
    public Result<List<PluginConfigIDTO>> listPlugins(@RequestParam(required = false) String pluginCode,
                                                       @RequestParam(required = false) String pluginName) {
        List<PluginConfigIDTO> plugins = pluginConfigApplicationService.listPlugins(pluginCode, pluginName);
        return Result.success(plugins);
    }

    @DeleteMapping("/{pluginCode}")
    public Result<String> deletePlugin(@PathVariable String pluginCode) {
        pluginConfigApplicationService.deletePlugin(pluginCode);
        return Result.success("删除成功");
    }

    @PostMapping("/{pluginCode}/enable")
    public Result<PluginConfigIDTO> enablePlugin(@PathVariable String pluginCode) {
        PluginConfigIDTO plugin = pluginConfigApplicationService.enablePlugin(pluginCode);
        return Result.success(plugin);
    }

    @PostMapping("/{pluginCode}/disable")
    public Result<PluginConfigIDTO> disablePlugin(@PathVariable String pluginCode) {
        PluginConfigIDTO plugin = pluginConfigApplicationService.disablePlugin(pluginCode);
        return Result.success(plugin);
    }
}
