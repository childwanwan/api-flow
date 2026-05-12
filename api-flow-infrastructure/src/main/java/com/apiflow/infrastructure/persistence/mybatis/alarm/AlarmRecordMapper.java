package com.apiflow.infrastructure.persistence.mybatis.alarm;

import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRecordPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmRecordMapper extends BaseMapper<AlarmRecordPO> {
}
