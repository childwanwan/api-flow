package com.apiflow.infrastructure.persistence.mybatis.alarm;

import com.apiflow.api.repository.alarm.AlarmRuleRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRuleIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRuleParam;
import com.apiflow.api.repository.alarm.param.UpdateAlarmRuleParam;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRulePO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createSimpleFieldResolver;

@Repository
@RequiredArgsConstructor
public class AlarmRuleRepositoryImpl implements AlarmRuleRepository {

    private final AlarmRuleMapper alarmRuleMapper;

    @Override
    public AlarmRuleIDTO save(SaveAlarmRuleParam param) {
        AlarmRulePO po = AlarmRulePO.builder()
                .ruleName(param.getRuleName())
                .alarmType(param.getAlarmType())
                .triggerCondition(param.getTriggerCondition())
                .level(param.getLevel())
                .enabled(param.getEnabled())
                .createTimeMs(param.getCreateTimeMs())
                .updateTimeMs(param.getUpdateTimeMs())
                .build();
        alarmRuleMapper.insert(po);
        return toIDTO(po);
    }

    @Override
    public AlarmRuleIDTO update(UpdateAlarmRuleParam param) {
        AlarmRulePO po = alarmRuleMapper.selectById(param.getId());
        if (po == null) return null;
        if (param.getRuleName() != null) po.setRuleName(param.getRuleName());
        if (param.getAlarmType() != null) po.setAlarmType(param.getAlarmType());
        if (param.getTriggerCondition() != null) po.setTriggerCondition(param.getTriggerCondition());
        if (param.getLevel() != null) po.setLevel(param.getLevel());
        if (param.getEnabled() != null) po.setEnabled(param.getEnabled());
        if (param.getUpdateTimeMs() != null) po.setUpdateTimeMs(param.getUpdateTimeMs());
        alarmRuleMapper.updateById(po);
        return toIDTO(po);
    }

    @Override
    public AlarmRuleIDTO selectById(Long id) {
        AlarmRulePO po = alarmRuleMapper.selectById(id);
        return po != null ? toIDTO(po) : null;
    }

    @Override
    public List<AlarmRuleIDTO> selectList(SelectAlarmRuleParam param) {
        LambdaQueryWrapper<AlarmRulePO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRulePO::getAlarmType, param.getAlarmType());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRulePO::getEnabled, param.getEnabled());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createSimpleFieldResolver());
        wrapper.orderByDesc(AlarmRulePO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<AlarmRulePO> list = alarmRuleMapper.selectList(wrapper);
        return list.stream().map(this::toIDTO).collect(Collectors.toList());
    }

    @Override
    public long delete(Long id) {
        return alarmRuleMapper.deleteById(id);
    }

    private AlarmRuleIDTO toIDTO(AlarmRulePO po) {
        return AlarmRuleIDTO.builder()
                .id(po.getId())
                .ruleName(po.getRuleName())
                .alarmType(po.getAlarmType())
                .triggerCondition(po.getTriggerCondition())
                .level(po.getLevel())
                .enabled(po.getEnabled())
                .createTimeMs(po.getCreateTimeMs())
                .updateTimeMs(po.getUpdateTimeMs())
                .build();
    }
}
