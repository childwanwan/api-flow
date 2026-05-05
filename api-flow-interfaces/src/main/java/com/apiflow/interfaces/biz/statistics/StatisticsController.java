package com.apiflow.interfaces.biz.statistics;

import com.apiflow.application.statistics.StatisticsService;
import com.apiflow.common.result.Result;
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

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public Result<StatisticsService.DashboardData> getDashboardData() {
        StatisticsService.DashboardData data = statisticsService.getDashboardData();
        return Result.success(data);
    }
}
