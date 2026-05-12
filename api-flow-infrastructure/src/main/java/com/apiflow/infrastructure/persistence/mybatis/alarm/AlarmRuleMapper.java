package com.apiflow.infrastructure.persistence.mybatis.alarm;

import com.apiflow.infrastructure.persistence.mybatis.alarm.entity.AlarmRulePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmRuleMapper extends BaseMapper<AlarmRulePO> {
}
