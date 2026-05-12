package com.apiflow.infrastructure.persistence.mybatis.configlog;

import com.apiflow.api.repository.configlog.ConfigChangeLogRepository;
import com.apiflow.api.repository.configlog.idto.ConfigChangeLogIDTO;
import com.apiflow.api.repository.configlog.param.SaveConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.SelectConfigChangeLogParam;
import com.apiflow.api.repository.configlog.param.ConfigChangeLogField;
import com.apiflow.infrastructure.persistence.mybatis.configlog.converter.ConfigChangeLogConverter;
import com.apiflow.infrastructure.persistence.mybatis.configlog.entity.ConfigChangeLogPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public ConfigChangeLogIDTO save(SaveConfigChangeLogParam param) {
        ConfigChangeLogPO po = ConfigChangeLogConverter.INSTANCE.saveParamToPO(param);
        configChangeLogMapper.insert(po);
        return ConfigChangeLogConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<ConfigChangeLogIDTO> selectList(SelectConfigChangeLogParam param) {
        LambdaQueryWrapper<ConfigChangeLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getChangeType, param.getChangeType());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ConfigChangeLogField.values()));
        wrapper.orderByDesc(ConfigChangeLogPO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<ConfigChangeLogPO> list = configChangeLogMapper.selectList(wrapper);
        return list.stream()
                .map(ConfigChangeLogConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SelectConfigChangeLogParam param) {
        LambdaQueryWrapper<ConfigChangeLogPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getApiCode, param.getApiCode());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getChangeType, param.getChangeType());
        QueryConditionHelper.applyFieldCondition(wrapper, ConfigChangeLogPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(ConfigChangeLogField.values()));
        return configChangeLogMapper.selectCount(wrapper);
    }
}
