package com.apiflow.interfaces.biz.statistics;

import com.apiflow.application.statistics.StatisticsApplicationService;
import com.apiflow.common.result.Result;
import com.apiflow.interfaces.biz.statistics.converter.StatisticsConverter;
import com.apiflow.interfaces.biz.statistics.vo.DashboardDataVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsApplicationService statisticsApplicationService;

    @GetMapping("/dashboard")
    public Result<DashboardDataVO> getDashboardData() {
        StatisticsApplicationService.DashboardData data = statisticsApplicationService.getDashboardData();
        return Result.success(StatisticsConverter.INSTANCE.toVO(data));
    }
}
