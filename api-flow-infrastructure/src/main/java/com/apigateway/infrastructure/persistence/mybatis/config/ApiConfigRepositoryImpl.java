package com.apigateway.infrastructure.persistence.mybatis.config;

import com.apigateway.domain.config.model.ApiConfigEntity;
import com.apigateway.domain.config.query.ApiConfigQuery;
import com.apigateway.domain.config.repository.ApiConfigRepository;
import com.apigateway.infrastructure.persistence.mybatis.config.entity.ApiConfigDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ApiConfigRepositoryImpl implements ApiConfigRepository {

    private final ApiConfigMapper apiConfigMapper;
    private final ApiConfigConverter apiConfigConverter;

    @Override
    public ApiConfigEntity save(ApiConfigEntity config) {
        ApiConfigDO configDO = apiConfigConverter.toDO(config);
        apiConfigMapper.insert(configDO);
        return apiConfigConverter.toEntity(configDO);
    }

    @Override
    public ApiConfigEntity update(ApiConfigEntity config) {
        ApiConfigDO configDO = apiConfigConverter.toDO(config);
        apiConfigMapper.updateById(configDO);
        return apiConfigConverter.toEntity(configDO);
    }

    @Override
    public ApiConfigEntity query(ApiConfigQuery query) {
        LambdaQueryWrapper<ApiConfigDO> wrapper = buildQueryWrapper(query);
        ApiConfigDO configDO = apiConfigMapper.selectOne(wrapper);
        return configDO == null ? null : apiConfigConverter.toEntity(configDO);
    }

    @Override
    public List<ApiConfigEntity> queryList(ApiConfigQuery query) {
        LambdaQueryWrapper<ApiConfigDO> wrapper = buildQueryWrapper(query);
        List<ApiConfigDO> configDOList = apiConfigMapper.selectList(wrapper);
        return configDOList.stream()
                .map(apiConfigConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exists(ApiConfigQuery query) {
        LambdaQueryWrapper<ApiConfigDO> wrapper = buildQueryWrapper(query);
        return apiConfigMapper.exists(wrapper);
    }

    private LambdaQueryWrapper<ApiConfigDO> buildQueryWrapper(ApiConfigQuery query) {
        LambdaQueryWrapper<ApiConfigDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getApiCode() != null) {
            wrapper.eq(ApiConfigDO::getApiCode, query.getApiCode());
        }
        if (query.getGroupNo() != null) {
            wrapper.eq(ApiConfigDO::getGroupNo, query.getGroupNo());
        }
        if (query.getEnabled() != null) {
            wrapper.eq(ApiConfigDO::getStatus, query.getEnabled() ? "ENABLED" : "DISABLED");
        }
        return wrapper;
    }

}
