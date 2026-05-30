package com.apiflow.infrastructure.persistence.mybatis.configlog;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.configlog.ConfigChangeLogRepository;
import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.SelectConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.ConfigChangeLogField;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.infrastructure.persistence.mybatis.configlog.converter.ConfigChangeLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.configlog.entity.ConfigChangeLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class ConfigChangeLogRepositoryImpl implements ConfigChangeLogRepository {

    private final ConfigChangeLogMapper configChangeLogMapper;

    @Override
    public void save(SaveConfigChangeLogParam param) {
        ConfigChangeLogPO po = ConfigChangeLogConverter.INSTANCE.saveParamToPO(param);
        configChangeLogMapper.insert(po);
    }

    @Override
    public List<ConfigChangeLogIDTO> selectList(SelectConfigChangeLogParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ConfigChangeLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.CHANGE_TYPE.getColumnName(), param.getChangeType());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ConfigChangeLogField.values()));
        wrapper.orderByDesc(ConfigChangeLogField.CREATE_TIME_MS.getColumnName());
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<ConfigChangeLogPO> list = configChangeLogMapper.selectList(wrapper);
        return list.stream()
                .map(ConfigChangeLogConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SelectConfigChangeLogParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<ConfigChangeLogPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.API_CODE.getColumnName(), param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.CHANGE_TYPE.getColumnName(), param.getChangeType());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ConfigChangeLogField.values()));
        return configChangeLogMapper.selectCount(wrapper);
    }
}
