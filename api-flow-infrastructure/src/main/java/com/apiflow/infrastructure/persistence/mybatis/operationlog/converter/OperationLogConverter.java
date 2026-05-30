package com.apiflow.infrastructure.persistence.mybatis.operationlog.converter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.apiflow.api.repository.operationlog.idto.OperationLogIDTO;
import com.apiflow.api.repository.operationlog.param.SaveOperationLogParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.infrastructure.persistence.mybatis.operationlog.entity.OperationLogPO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import tools.jackson.core.type.TypeReference;

import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface OperationLogConverter {

    OperationLogConverter INSTANCE = Mappers.getMapper(OperationLogConverter.class);

    @Mapping(target = "id", ignore = true)
    OperationLogPO saveParamToPO(SaveOperationLogParam param);

    @AfterMapping
    default void afterToSaveParam(SaveOperationLogParam source, @MappingTarget OperationLogPO target) {
        if (ObjectUtil.isEmpty(target)) {
            return;
        }
        target.setCreateTimeMs(System.currentTimeMillis());
    }

    @Mapping(target = "showDetail", ignore = true)
    OperationLogIDTO poToIDTO(OperationLogPO po);

    @AfterMapping
    default void afterPoToIDTO(OperationLogPO source, @MappingTarget OperationLogIDTO target) {
        if (StrUtil.isNotBlank(source.getLogData())) {
            try {
                Map<String, Object> map = JsonUtil.toObject(source.getLogData(), new TypeReference<Map<String, Object>>() {});
                if (map != null) {
                    Object val = map.get("showDetail");
                    target.setShowDetail(val != null ? val.toString() : null);
                }
            } catch (Exception ignored) {
            }
        }
    }

    default PageResult<OperationLogIDTO> operationLogPOIPage2PageResult(IPage<OperationLogPO> result) {
        if (result == null) {
            return null;
        }
        return PageResult.<OperationLogIDTO>builder()
                .records(result.getRecords().stream()
                        .map(this::poToIDTO)
                        .collect(Collectors.toList()))
                .total(result.getTotal())
                .size(result.getSize())
                .current(result.getCurrent())
                .pages(result.getPages())
                .build();
    }
}
