package com.apiflow.interfaces.biz.statistics.converter;

import com.apiflow.application.statistics.StatisticsApplicationService;
import com.apiflow.interfaces.biz.statistics.vo.DashboardDataVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatisticsConverter {
    StatisticsConverter INSTANCE = Mappers.getMapper(StatisticsConverter.class);

    DashboardDataVO toVO(StatisticsApplicationService.DashboardData data);
}
