package com.apiflow.interfaces.biz.configlog;

import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.application.configlog.ConfigChangeLogApplicationService;
import com.apiflow.application.configlog.param.ListConfigChangeLogParam;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.configlog.converter.ConfigChangeLogConverter;
import com.apiflow.interfaces.biz.configlog.vo.ConfigChangeLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/config-log")
public class ConfigChangeLogController {

    private static final ConfigChangeLogConverter LOG_CONVERTER = ConfigChangeLogConverter.INSTANCE;

    private final ConfigChangeLogApplicationService configChangeLogApplicationService;

    @GetMapping("/list")
    public Result<List<ConfigChangeLogVO>> listLogs(@RequestParam(required = false) String apiCode,
                                                       @RequestParam(required = false) String changeType,
                                                       @RequestParam(required = false) Long startTimeMs,
                                                       @RequestParam(required = false) Long endTimeMs,
                                                       @RequestParam(required = false, defaultValue = "20") Integer limit) {
        ListConfigChangeLogParam param = ListConfigChangeLogParam.builder()
                .apiCode(apiCode)
                .changeType(changeType)
                .startTimeMs(startTimeMs)
                .endTimeMs(endTimeMs)
                .limit(limit)
                .build();
        List<ConfigChangeLogIDTO> logs = configChangeLogApplicationService.listLogs(param);
        return Result.success(LOG_CONVERTER.toVOList(logs));
    }
}
