package com.apiflow.interfaces.biz.operationlog.converter;

import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.PageOperationLogParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.interfaces.biz.operationlog.request.OperationLogPageRequest;
import com.apiflow.interfaces.biz.operationlog.vo.OperationLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OperationLogConverter {

    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    OperationLogVO toVO(OperationLogDTO dto);

    PageOperationLogParam operationLogPageRequest2PageOperationLogParam(OperationLogPageRequest request);

    default PageResult<OperationLogVO> operationLogDTOPage2VO(PageResult<OperationLogDTO> pageResult) {
        if (pageResult == null) {
            return null;
        }
        List<OperationLogVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .toList();
        return PageResult.of(voList, pageResult.getTotal(), pageResult.getCurrent(), pageResult.getSize());
    }
}
