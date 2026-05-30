package com.apiflow.interfaces.biz.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataVO {
    private long totalTasks;
    private long pendingTasks;
    private long runningTasks;
    private long successTasks;
    private long failedTasks;
    private long canceledTasks;
    private long todayTotal;
    private long todaySuccess;
    private long todayFailed;
    private double successRate;
    private double failureRate;
}
