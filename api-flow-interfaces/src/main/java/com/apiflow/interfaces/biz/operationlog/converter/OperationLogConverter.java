package com.apiflow.interfaces.biz.operationlog.converter;

import com.apiflow.application.operationlog.dto.OperationLogDTO;
import com.apiflow.application.operationlog.param.OperationLogPageParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.interfaces.biz.operationlog.request.OperationLogPageRequest;
import com.apiflow.interfaces.biz.operationlog.vo.OperationLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OperationLogConverter {

    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    OperationLogVO toVO(OperationLogDTO dto);

    OperationLogPageParam operationLogPageRequest2OperationLogPageParam(OperationLogPageRequest request);

    PageResult<OperationLogVO> operationLogDTOPage2VO(PageResult<OperationLogDTO> pageResult);
}
