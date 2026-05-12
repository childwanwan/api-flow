package com.apiflow.interfaces.biz.operationlog;

import com.apiflow.application.operationlog.OperationLogService;
import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.OperationLogPageParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.operationlog.converter.OperationLogConverter;
import com.apiflow.interfaces.biz.operationlog.request.OperationLogPageRequest;
import com.apiflow.interfaces.biz.operationlog.vo.OperationLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operation-log")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @PostMapping("/page")
    public Result<PageResult<OperationLogVO>> page(@RequestBody OperationLogPageRequest request) {
        validateOperationLogPageRequest(request);
        OperationLogPageParam pageParam = OperationLogConverter.INSTANCE.operationLogPageRequest2OperationLogPageParam(request);
        PageResult<OperationLogDTO> pageResult = operationLogService.pageLogs(pageParam);
        return Result.success(OperationLogConverter.INSTANCE.operationLogDTOPage2VO(pageResult));
    }

    private void validateOperationLogPageRequest(OperationLogPageRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        request.validateBasePageParam();
    }
}
