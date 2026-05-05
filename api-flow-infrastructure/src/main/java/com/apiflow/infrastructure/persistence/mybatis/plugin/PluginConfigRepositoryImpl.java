package com.apiflow.infrastructure.persistence.mybatis.plugin;

import com.apiflow.api.repository.plugin.PluginConfigRepository;
import com.apiflow.api.repository.plugin.idto.PluginConfigIDTO;
import com.apiflow.api.repository.plugin.param.SavePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectOnePluginConfigParam;
import com.apiflow.api.repository.plugin.param.SelectPluginConfigParam;
import com.apiflow.api.repository.plugin.param.UpdatePluginConfigParam;
import com.apiflow.infrastructure.persistence.mybatis.plugin.converter.PluginConfigConverter;
import com.apiflow.infrastructure.persistence.mybatis.plugin.entity.PluginConfigPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PluginConfigRepositoryImpl implements PluginConfigRepository {

    private final PluginConfigMapper pluginConfigMapper;

    @Override
    public PluginConfigIDTO save(SavePluginConfigParam param) {
        PluginConfigPO po = PluginConfigConverter.INSTANCE.saveParamToPO(param);
        pluginConfigMapper.insert(po);
        return PluginConfigConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public PluginConfigIDTO update(UpdatePluginConfigParam param) {
        PluginConfigPO po = PluginConfigConverter.INSTANCE.updateParamToPO(param);
        pluginConfigMapper.updateById(po);
        return PluginConfigConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public PluginConfigIDTO selectOne(SelectOnePluginConfigParam param) {
        LambdaQueryWrapper<PluginConfigPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigPO::getPluginCode, param.getPluginCode());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigPO::getPluginName, param.getPluginName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        PluginConfigPO po = pluginConfigMapper.selectOne(wrapper);
        return po == null ? null : PluginConfigConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<PluginConfigIDTO> selectList(SelectPluginConfigParam param) {
        LambdaQueryWrapper<PluginConfigPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigPO::getPluginCode, param.getPluginCode());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigPO::getPluginName, param.getPluginName());
        QueryConditionHelper.applyFieldCondition(wrapper, PluginConfigPO::getEnabled, param.getEnabled());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        wrapper.orderByAsc(PluginConfigPO::getOrderNum);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<PluginConfigPO> list = pluginConfigMapper.selectList(wrapper);
        return list.stream()
                .map(PluginConfigConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }
}
