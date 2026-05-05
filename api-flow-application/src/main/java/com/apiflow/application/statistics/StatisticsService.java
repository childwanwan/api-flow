package com.apiflow.application.statistics;

import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SelectTaskParam;
import com.apiflow.common.enums.TaskStatus;
import com.apiflow.common.repository.FieldCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TaskRepository taskRepository;

    public DashboardData getDashboardData() {
        long now = System.currentTimeMillis();
        long todayStart = now - (now % 86400000);

        long totalTasks = countByStatus(null, null);
        long pendingTasks = countByStatus(TaskStatus.PENDING.getValue(), null);
        long runningTasks = countByStatus(TaskStatus.RUNNING.getValue(), null);
        long successTasks = countByStatus(TaskStatus.SUCCESS.getValue(), null);
        long failedTasks = countByStatus(TaskStatus.FAILED.getValue(), null);
        long canceledTasks = countByStatus(TaskStatus.CANCELED.getValue(), null);

        long todayTotal = countByStatus(null, todayStart);
        long todaySuccess = countByStatus(TaskStatus.SUCCESS.getValue(), todayStart);
        long todayFailed = countByStatus(TaskStatus.FAILED.getValue(), todayStart);

        double successRate = todayTotal > 0 ? (double) todaySuccess / todayTotal * 100 : 0;
        double failureRate = todayTotal > 0 ? (double) todayFailed / todayTotal * 100 : 0;

        return DashboardData.builder()
                .totalTasks(totalTasks)
                .pendingTasks(pendingTasks)
                .runningTasks(runningTasks)
                .successTasks(successTasks)
                .failedTasks(failedTasks)
                .canceledTasks(canceledTasks)
                .todayTotal(todayTotal)
                .todaySuccess(todaySuccess)
                .todayFailed(todayFailed)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .failureRate(Math.round(failureRate * 100.0) / 100.0)
                .build();
    }

    private long countByStatus(String status, Long startTimeMs) {
        SelectTaskParam.SelectTaskParamBuilder builder = SelectTaskParam.builder();
        if (status != null) {
            builder.status(FieldCondition.of(status));
        }
        if (startTimeMs != null) {
            builder.createTimeMs(FieldCondition.<Long>builder().ge(startTimeMs).build());
        }
        builder.limit(10000);
        List<TaskIDTO> tasks = taskRepository.selectList(builder.build());
        return tasks.size();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardData {
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
}
