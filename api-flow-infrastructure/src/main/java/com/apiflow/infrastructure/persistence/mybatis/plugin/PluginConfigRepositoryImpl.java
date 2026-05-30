package com.apiflow.infrastructure.persistence.mybatis.plugin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectPluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;
import com.apiflow.api.repository.plugin.param.PluginConfigField;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.infrastructure.persistence.mybatis.plugin.converter.PluginConfigConverter;
import com.apiflow.infrastructure.persistence.mybatis.plugin.entity.PluginConfigPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class PluginConfigRepositoryImpl implements PluginConfigRepository {

    private final PluginConfigMapper pluginConfigMapper;

    @Override
    public PluginConfigIDTO findByPluginCode(String pluginCode) {
        QueryWrapper<PluginConfigPO> wrapper = new QueryWrapper<>();
        wrapper.eq(PluginConfigField.PLUGIN_CODE.getColumnName(), pluginCode);
        wrapper.last("LIMIT 1");
        List<PluginConfigPO> list = pluginConfigMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return PluginConfigConverter.INSTANCE.poToIDTO(list.get(0));
    }

    @Override
    public void save(SavePluginConfigParam param) {
        PluginConfigPO po = PluginConfigConverter.INSTANCE.saveParamToPO(param);
        pluginConfigMapper.insert(po);
    }

    @Override
    public void update(UpdatePluginConfigParam param) {
        PluginConfigPO po = PluginConfigConverter.INSTANCE.updateParamToPO(param);
        pluginConfigMapper.updateById(po);
    }

    @Override
    public PluginConfigIDTO selectOne(SelectOnePluginConfigParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<PluginConfigPO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigField.PLUGIN_CODE.getColumnName(), param.getPluginCode());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigField.PLUGIN_NAME.getColumnName(), param.getPluginName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(PluginConfigField.values()));
        wrapper.orderByDesc("id").last("LIMIT 1");
        List<PluginConfigPO> list = pluginConfigMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return PluginConfigConverter.INSTANCE.poToIDTO(list.get(0));
    }

    @Override
    public List<PluginConfigIDTO> selectList(SelectPluginConfigParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<PluginConfigPO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigField.PLUGIN_CODE.getColumnName(), param.getPluginCode());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigField.PLUGIN_NAME.getColumnName(), param.getPluginName());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigField.ENABLED.getColumnName(), param.getEnabled());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(PluginConfigField.values()));
        wrapper.orderByAsc(PluginConfigField.ORDER_NUM.getColumnName());
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<PluginConfigPO> list = pluginConfigMapper.selectList(wrapper);
        return list.stream()
                .map(PluginConfigConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }
}
