package com.apiflow.infrastructure.persistence.mybatis.alarm;

import cn.hutool.core.util.ObjectUtil;
import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.param.AlarmRecordField;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRecordParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.infrastructure.persistence.mybatis.alarm.converter.AlarmRecordConverter;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRecordPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class AlarmRecordRepositoryImpl implements AlarmRecordRepository {

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public void save(SaveAlarmRecordParam param) {
        AlarmRecordPO po = AlarmRecordConverter.INSTANCE.saveParamToPO(param);
        alarmRecordMapper.insert(po);
    }

    @Override
    public List<AlarmRecordIDTO> selectList(SelectAlarmRecordParam param) {
        if (ObjectUtil.isEmpty(param)) {
            throw new BusinessException(ErrorCode.PARAM_IS_EMPTY);
        }
        QueryWrapper<AlarmRecordPO> wrapper = new QueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordField.EVENT_TYPE.getColumnName(), param.getEventType());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordField.LEVEL.getColumnName(), param.getLevel());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordField.CREATE_TIME_MS.getColumnName(), param.getCreateTimeMs());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(AlarmRecordField.values()));
        wrapper.orderByDesc(AlarmRecordField.CREATE_TIME_MS.getColumnName());
        wrapper.last("LIMIT " + param.getEffectiveLimit());
        List<AlarmRecordPO> list = alarmRecordMapper.selectList(wrapper);
        return list.stream().map(AlarmRecordConverter.INSTANCE::poToIDTO).collect(Collectors.toList());
    }
}
