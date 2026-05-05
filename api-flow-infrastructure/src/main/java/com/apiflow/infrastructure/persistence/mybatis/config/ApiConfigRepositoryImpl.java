package com.apiflow.infrastructure.persistence.mybatis.config;

import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.*;
import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.infrastructure.persistence.mybatis.config.converter.ApiConfigConverter;
import com.apiflow.infrastructure.persistence.mybatis.config.entity.ApiConfigPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        if (param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            applyConditions(wrapper, param);
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            ApiConfigPO configDO = apiConfigMapper.selectOne(wrapper);
            return configDO == null ? null : ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(configDO);
        }
        LambdaQueryWrapper<ApiConfigPO> wrapper = buildLambdaQueryWrapper(param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        ApiConfigPO configDO = apiConfigMapper.selectOne(wrapper);
        return configDO == null ? null : ApiConfigConverter.INSTANCE.apiConfigEntityPOToApiConfigIDTO(configDO);
    }

    @Override
    public List<ApiConfigIDTO> selectList(SelectApiConfigParam param) {
        if (param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<ApiConfigPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            applyConditions(wrapper, param);
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            List<ApiConfigPO> configDOList = apiConfigMapper.selectList(wrapper);
            return configDOList.stream()
                    .map(ApiConfigConverter.INSTANCE::apiConfigEntityPOToApiConfigIDTO)
                    .collect(Collectors.toList());
        }
        LambdaQueryWrapper<ApiConfigPO> wrapper = buildLambdaQueryWrapper(param);
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        List<ApiConfigPO> configDOList = apiConfigMapper.selectList(wrapper);
        return configDOList.stream()
                .map(ApiConfigConverter.INSTANCE::apiConfigEntityPOToApiConfigIDTO)
                .collect(Collectors.toList());
    }

    private void applyConditions(QueryWrapper<ApiConfigPO> wrapper, SelectOneApiConfigParam param) {
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.GROUP_NO.getColumnName(), param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.STATUS.getColumnName(), param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.API_NAME.getColumnName(), param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.REQUEST_TIMEOUT_MS.getColumnName(), param.getRequestTimeoutMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.AUTO_RETRY_COUNT.getColumnName(), param.getAutoRetryCount());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.MAX_QUEUE_SIZE.getColumnName(), param.getMaxQueueSize());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.UPDATE_TIME_MS.getColumnName(), param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "maxRequests", param.getRateLimitConfigMaxRequests());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "timeWindowMs", param.getRateLimitConfigTimeWindowMs());
    }

    private void applyConditions(QueryWrapper<ApiConfigPO> wrapper, SelectApiConfigParam param) {
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.GROUP_NO.getColumnName(), param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.STATUS.getColumnName(), param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.API_NAME.getColumnName(), param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.REQUEST_TIMEOUT_MS.getColumnName(), param.getRequestTimeoutMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.AUTO_RETRY_COUNT.getColumnName(), param.getAutoRetryCount());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.MAX_QUEUE_SIZE.getColumnName(), param.getMaxQueueSize());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigField.UPDATE_TIME_MS.getColumnName(), param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "maxRequests", param.getRateLimitConfigMaxRequests());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "timeWindowMs", param.getRateLimitConfigTimeWindowMs());
    }

    private LambdaQueryWrapper<ApiConfigPO> buildLambdaQueryWrapper(SelectOneApiConfigParam param) {
        LambdaQueryWrapper<ApiConfigPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getStatus, param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getApiName, param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getRequestTimeoutMs, param.getRequestTimeoutMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getAutoRetryCount, param.getAutoRetryCount());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getMaxQueueSize, param.getMaxQueueSize());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getUpdateTimeMs, param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "maxRequests", param.getRateLimitConfigMaxRequests());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "timeWindowMs", param.getRateLimitConfigTimeWindowMs());
        return wrapper;
    }

    private LambdaQueryWrapper<ApiConfigPO> buildLambdaQueryWrapper(SelectApiConfigParam param) {
        LambdaQueryWrapper<ApiConfigPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getStatus, param.getStatus());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getApiName, param.getApiName());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getRequestTimeoutMs, param.getRequestTimeoutMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getAutoRetryCount, param.getAutoRetryCount());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getMaxQueueSize, param.getMaxQueueSize());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiConfigPO::getUpdateTimeMs, param.getUpdateTimeMs());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "maxRequests", param.getRateLimitConfigMaxRequests());
        QueryConditionHelper.applyJsonFieldCondition(wrapper, "rate_limit_config", "timeWindowMs", param.getRateLimitConfigTimeWindowMs());
        return wrapper;
    }

}
