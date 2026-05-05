package com.flow.infrastructure.persistence.mybatis.config;

import com.flow.api.repository.config.dto.ApiConfigIDTO;
import com.flow.api.repository.config.param.*;
import com.flow.api.repository.config.ApiConfigRepository;
import com.flow.infrastructure.persistence.mybatis.config.converter.ApiConfigConverter;
import com.flow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ApiConfigRepositoryImpl implements ApiConfigRepository {

    private final ApiConfigMapper apiConfigMapper;

    @Override
    public ApiConfigIDTO save(SaveApiConfigParam param) {
        ApiConfigPO configDO = ApiConfigConverter.INSTANCE.saveApiConfigParamToApiConfigEntityPO(param);
        apiConfigMapper.insert(configDO);
        return ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(configDO);
    }

    @Override
    public ApiConfigIDTO update(UpdateApiConfigParam param) {
        ApiConfigPO configDO = ApiConfigConverter.INSTANCE.updateApiConfigParamToApiConfigEntityPO(param);
        apiConfigMapper.updateById(configDO);
        return ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(configDO);
    }

    @Override
    public ApiConfigIDTO selectOne(SelectOneApiConfigParam param) {
        QueryWrapper<ApiConfigPO> wrapper = buildQueryWrapper(param);
        applySelectFields(wrapper, param.getSelectFields());
        ApiConfigPO configDO = apiConfigMapper.selectOne(wrapper);
        return configDO == null ? null : ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(configDO);
    }

    @Override
    public List<ApiConfigIDTO> selectList(SelectApiConfigParam param) {
        QueryWrapper<ApiConfigPO> wrapper = buildQueryWrapper(param);
        applySelectFields(wrapper, param.getSelectFields());
        List<ApiConfigPO> configDOList = apiConfigMapper.selectList(wrapper);
        return configDOList.stream()
                .map(ApiConfigConverter.INSTANCE::apiConfigEntityPOToApiConfigIDTO)
                .collect(Collectors.toList());
    }

    private void applySelectFields(QueryWrapper<ApiConfigPO> wrapper, List<ApiConfigField> selectFields) {
        if (selectFields != null && !selectFields.isEmpty()) {
            List<String> columns = new ArrayList<>();
            for (ApiConfigField field : selectFields) {
                if (field.isJsonField()) {
                    String alias = field.getFieldName().replace(".", "_");
                    columns.add(String.format("JSON_UNQUOTE(JSON_EXTRACT(%s, '$.%s')) AS %s",
                            field.getColumnName(), field.getJsonPath(), alias));
                } else {
                    columns.add(field.getColumnName());
                }
            }
            wrapper.select(columns.toArray(new String[0]));
        }
    }

    private QueryWrapper<ApiConfigPO> buildQueryWrapper(SelectOneApiConfigParam param) {
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        if (param.getApiCode() != null) {
            wrapper.eq("api_code", param.getApiCode());
        }
        if (param.getGroupNo() != null) {
            wrapper.eq("group_no", param.getGroupNo());
        }
        if (param.getEnabled() != null) {
            wrapper.eq("status", param.getEnabled() ? "ENABLED" : "DISABLED");
        }
        return wrapper;
    }

    private QueryWrapper<ApiConfigPO> buildQueryWrapper(SelectApiConfigParam param) {
        QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
        if (param.getApiCode() != null) {
            wrapper.eq("api_code", param.getApiCode());
        }
        if (param.getGroupNo() != null) {
            wrapper.eq("group_no", param.getGroupNo());
        }
        if (param.getEnabled() != null) {
            wrapper.eq("status", param.getEnabled() ? "ENABLED" : "DISABLED");
        }
        return wrapper;
    }

}