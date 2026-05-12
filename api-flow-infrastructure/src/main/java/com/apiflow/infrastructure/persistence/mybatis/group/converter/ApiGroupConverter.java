package com.apiflow.infrastructure.persistence.mybatis.group.converter;

import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;
import com.apiflow.common.result.PageResult;
import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface ApiGroupConverter {

    ApiGroupConverter INSTANCE = Mappers.getMapper(ApiGroupConverter.class);

    @Named("saveParamToPO")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "version", ignore = true)
    ApiGroupPO saveParamToPO(SaveApiGroupParam param);

    @Named("updateParamToPO")
    ApiGroupPO updateParamToPO(UpdateApiGroupParam param);

    @Named("poToIDTO")
    ApiGroupIDTO poToIDTO(ApiGroupPO po);

    default PageResult<ApiGroupIDTO> apiGroupPOIPage2PageResult(IPage<ApiGroupPO> result) {
        if (result == null) {
            return null;
        }
        return PageResult.<ApiGroupIDTO>builder()
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
