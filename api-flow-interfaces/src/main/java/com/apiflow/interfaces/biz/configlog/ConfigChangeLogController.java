package com.apiflow.interfaces.biz.configlog;

import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.application.configlog.ConfigChangeLogService;
import com.apiflow.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/config-log")
public class ConfigChangeLogController {

    private final ConfigChangeLogService configChangeLogService;

    @GetMapping("/list")
    public Result<List<ConfigChangeLogIDTO>> listLogs(@RequestParam(required = false) String apiCode,
                                                       @RequestParam(required = false) String changeType,
                                                       @RequestParam(required = false) Long startTimeMs,
                                                       @RequestParam(required = false) Long endTimeMs,
                                                       @RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<ConfigChangeLogIDTO> logs = configChangeLogService.listLogs(apiCode, changeType, startTimeMs, endTimeMs, limit);
        return Result.success(logs);
    }
}
