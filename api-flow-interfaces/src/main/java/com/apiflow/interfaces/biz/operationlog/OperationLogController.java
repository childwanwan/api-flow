package com.apiflow.interfaces.biz.operationlog;

import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.application.operationlog.OperationLogService;
import com.apiflow.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operation-log")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping("/list")
    public Result<List<OperationLogIDTO>> listLogs(@RequestParam(required = false) String username,
                                                    @RequestParam(required = false) String operation,
                                                    @RequestParam(required = false) String module,
                                                    @RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<OperationLogIDTO> logs = operationLogService.listLogs(username, operation, module, limit);
        return Result.success(logs);
    }
}
