package com.apiflow.infrastructure.persistence.mybatis.alarm;

import com.apiflow.api.repository.alarm.AlarmRecordRepository;
import com.apiflow.api.repository.alarm.idto.AlarmRecordIDTO;
import com.apiflow.api.repository.alarm.param.SaveAlarmRecordParam;
import com.apiflow.api.repository.alarm.param.SelectAlarmRecordParam;
import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRecordPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createSimpleFieldResolver;

@Repository
@RequiredArgsConstructor
public class AlarmRecordRepositoryImpl implements AlarmRecordRepository {

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public AlarmRecordIDTO save(SaveAlarmRecordParam param) {
        AlarmRecordPO po = AlarmRecordPO.builder()
                .eventType(param.getEventType())
                .level(param.getLevel())
                .message(param.getMessage())
                .detail(param.getDetail())
                .taskNo(param.getTaskNo())
                .apiCode(param.getApiCode())
                .createTimeMs(param.getCreateTimeMs())
                .build();
        alarmRecordMapper.insert(po);
        return toIDTO(po);
    }

    @Override
    public List<AlarmRecordIDTO> selectList(SelectAlarmRecordParam param) {
        LambdaQueryWrapper<AlarmRecordPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordPO::getEventType, param.getEventType());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordPO::getLevel, param.getLevel());
        QueryConditionHelper.applyFieldCondition(wrapper, AlarmRecordPO::getCreateTimeMs, param.getCreateTimeMs());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createSimpleFieldResolver());
        wrapper.orderByDesc(AlarmRecordPO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<AlarmRecordPO> list = alarmRecordMapper.selectList(wrapper);
        return list.stream().map(this::toIDTO).collect(Collectors.toList());
    }

    private AlarmRecordIDTO toIDTO(AlarmRecordPO po) {
        return AlarmRecordIDTO.builder()
                .id(po.getId())
                .eventType(po.getEventType())
                .level(po.getLevel())
                .message(po.getMessage())
                .detail(po.getDetail())
                .taskNo(po.getTaskNo())
                .apiCode(po.getApiCode())
                .createTimeMs(po.getCreateTimeMs())
                .build();
    }
}
