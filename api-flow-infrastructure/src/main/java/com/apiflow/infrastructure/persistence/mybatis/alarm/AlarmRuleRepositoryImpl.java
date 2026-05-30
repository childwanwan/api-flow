package com.apiflow.infrastructure.persistence.mybatis.alarm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.alarm.AlarmRuleRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.AlarmRuleField;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectOneAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.infrastructure.persistence.mybatis.alarm.converter.AlarmRuleConverter;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRulePO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class AlarmRuleRepositoryImpl implements AlarmRuleRepository {

    private final AlarmRuleMapper alarmRuleMapper;

    @Override
    public void save(SaveAlarmRuleParam param) {
        AlarmRulePO po = AlarmRuleConverter.INSTANCE.saveParamToPO(param);
        alarmRuleMapper.insert(po);
    }

    @Override
    public void update(UpdateAlarmRuleParam param) {
        AlarmRulePO po = AlarmRuleConverter.INSTANCE.updateParamToPO(param);
        QueryWrapper<AlarmRulePO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", param.getId()).last("LIMIT 1");
        alarmRuleMapper.update(po, wrapper);
    }

    @Override
    public AlarmRuleIDTO selectById(Long id) {
        AlarmRulePO po = alarmRuleMapper.selectById(id);
        return po != null ? AlarmRuleConverter.INSTANCE.poToIDTO(po) : null;
    }

    @Override
    public AlarmRuleIDTO selectOne(SelectOneAlarmRuleParam param) {
        if (ObjectUtil.isEmpty(param) || param.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<AlarmRulePO> wrapper = new QueryWrapper<>();
        if (CollUtil.isNotEmpty(param.getSelectFields())) {
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
        }
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRuleField.ALARM_TYPE.getColumnName(), param.getAlarmType());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRuleField.ENABLED.getColumnName(), param.getEnabled());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(AlarmRuleField.values()));
        wrapper.orderByDesc("id").last("LIMIT 1");
        List<AlarmRulePO> list = alarmRuleMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return AlarmRuleConverter.INSTANCE.poToIDTO(list.get(0));
    }

    @Override
    public List<AlarmRuleIDTO> selectList(SelectAlarmRuleParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<AlarmRulePO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRuleField.ALARM_TYPE.getColumnName(), param.getAlarmType());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRuleField.ENABLED.getColumnName(), param.getEnabled());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(AlarmRuleField.values()));
        wrapper.orderByDesc(AlarmRuleField.CREATE_TIME_MS.getColumnName());
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<AlarmRulePO> list = alarmRuleMapper.selectList(wrapper);
        return list.stream().map(AlarmRuleConverter.INSTANCE::poToIDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return;
        }
        alarmRuleMapper.deleteByIds(idList);
    }
}
