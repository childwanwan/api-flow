package com.apiflow.interfaces.biz.plugin;

import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.application.plugin.PluginConfigApplicationService;
import com.apiflow.application.plugin.param.CreatePluginConfigParam;
import com.apiflow.application.plugin.param.DeletePluginConfigParam;
import com.apiflow.application.plugin.param.DisablePluginConfigParam;
import com.apiflow.application.plugin.param.EnablePluginConfigParam;
import com.apiflow.application.plugin.param.GetPluginConfigParam;
import com.apiflow.application.plugin.param.ListPluginConfigParam;
import com.apiflow.application.plugin.param.UpdatePluginConfigParam;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.plugin.converter.PluginConfigConverter;
import com.apiflow.interfaces.biz.plugin.vo.PluginConfigVO;
import com.apiflow.interfaces.biz.plugin.request.PluginConfigCreateRequest;
import com.apiflow.interfaces.biz.plugin.request.PluginConfigUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plugin")
public class PluginConfigController {

    private static final PluginConfigConverter PLUGIN_CONVERTER = PluginConfigConverter.INSTANCE;

    private final PluginConfigApplicationService pluginConfigApplicationService;

    @PostMapping("/create")
    public Result<PluginConfigVO> createPlugin(@RequestBody PluginConfigCreateRequest request) {
        CreatePluginConfigParam param = CreatePluginConfigParam.builder()
                .pluginCode(request.getPluginCode())
                .pluginName(request.getPluginName())
                .pluginClass(request.getPluginClass())
                .description(request.getDescription())
                .config(request.getConfig())
                .enabled(request.getEnabled())
                .orderNum(request.getOrderNum())
                .build();
        PluginConfigIDTO plugin = pluginConfigApplicationService.createPlugin(param);
        return Result.success(PLUGIN_CONVERTER.toVO(plugin));
    }

    @PutMapping("/update")
    public Result<PluginConfigVO> updatePlugin(@RequestBody PluginConfigUpdateRequest request) {
        UpdatePluginConfigParam param = UpdatePluginConfigParam.builder()
                .pluginCode(request.getPluginCode())
                .pluginName(request.getPluginName())
                .pluginClass(request.getPluginClass())
                .description(request.getDescription())
                .config(request.getConfig())
                .enabled(request.getEnabled())
                .orderNum(request.getOrderNum())
                .build();
        PluginConfigIDTO plugin = pluginConfigApplicationService.updatePlugin(param);
        return Result.success(PLUGIN_CONVERTER.toVO(plugin));
    }

    @GetMapping("/{pluginCode}")
    public Result<PluginConfigVO> getPlugin(@PathVariable String pluginCode) {
        GetPluginConfigParam param = GetPluginConfigParam.builder()
                .pluginCode(pluginCode)
                .build();
        PluginConfigIDTO plugin = pluginConfigApplicationService.getPlugin(param);
        return Result.success(PLUGIN_CONVERTER.toVO(plugin));
    }

    @GetMapping("/list")
    public Result<List<PluginConfigVO>> listPlugins(@RequestParam(required = false) String pluginCode,
                                                       @RequestParam(required = false) String pluginName) {
        ListPluginConfigParam param = ListPluginConfigParam.builder()
                .pluginCode(pluginCode)
                .pluginName(pluginName)
                .build();
        List<PluginConfigIDTO> plugins = pluginConfigApplicationService.listPlugins(param);
        return Result.success(PLUGIN_CONVERTER.toVOList(plugins));
    }

    @DeleteMapping("/{pluginCode}")
    public Result<Void> deletePlugin(@PathVariable String pluginCode) {
        DeletePluginConfigParam param = DeletePluginConfigParam.builder()
                .pluginCode(pluginCode)
                .build();
        pluginConfigApplicationService.deletePlugin(param);
        return Result.success();
    }

    @PostMapping("/{pluginCode}/enable")
    public Result<PluginConfigVO> enablePlugin(@PathVariable String pluginCode) {
        EnablePluginConfigParam param = EnablePluginConfigParam.builder()
                .pluginCode(pluginCode)
                .build();
        PluginConfigIDTO plugin = pluginConfigApplicationService.enablePlugin(param);
        return Result.success(PLUGIN_CONVERTER.toVO(plugin));
    }

    @PostMapping("/{pluginCode}/disable")
    public Result<PluginConfigVO> disablePlugin(@PathVariable String pluginCode) {
        DisablePluginConfigParam param = DisablePluginConfigParam.builder()
                .pluginCode(pluginCode)
                .build();
        PluginConfigIDTO plugin = pluginConfigApplicationService.disablePlugin(param);
        return Result.success(PLUGIN_CONVERTER.toVO(plugin));
    }
}
