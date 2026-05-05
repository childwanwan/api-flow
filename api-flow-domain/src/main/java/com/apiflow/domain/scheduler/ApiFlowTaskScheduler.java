package com.apiflow.domain.scheduler;

import com.apiflow.api.repository.task.TaskRepository;
import com.apiflow.api.repository.task.idto.TaskIDTO;
import com.apiflow.api.repository.task.param.SelectTaskParam;
import com.apiflow.api.repository.task.param.UpdateTaskParam;
import com.apiflow.api.repository.tasklog.TaskLogRepository;
import com.apiflow.api.repository.tasklog.param.SaveTaskLogParam;
import com.apiflow.common.constant.SystemConstant;
import com.apiflow.common.enums.CompensateStatus;
import com.apiflow.common.enums.TaskStatus;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.util.JsonUtil;
import com.apiflow.domain.alarm.AlarmEvent;
import com.apiflow.domain.alarm.AlarmSender;
import com.apiflow.domain.lock.DistributedLock;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.receipt.ReceiptService;
import com.apiflow.domain.task.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiFlowTaskScheduler implements SmartLifecycle {

    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;
    private final DistributedLock distributedLock;
    private final PluginChainExecutor pluginChainExecutor;
    private final ReceiptService receiptService;
    private final AlarmSender alarmSender;

    private final ConcurrentMap<String, AtomicBoolean> localInterruptFlags = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2,
            r -> {
                Thread t = new Thread(r, "task-executor");
                t.setDaemon(true);
                return t;
            }
    );
    private final ScheduledExecutorService retryScheduler = Executors.newSingleThreadScheduledExecutor(
            r -> {
                Thread t = new Thread(r, "retry-scheduler");
                t.setDaemon(true);
                return t;
            }
    );

    private final Object wakeLock = new Object();
    private volatile boolean running = false;
    private Thread dispatchThread;

    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        dispatchThread = new Thread(this::dispatchLoop, "task-dispatcher");
        dispatchThread.setDaemon(true);
        dispatchThread.start();
        log.info("ApiFlowTaskScheduler dispatch thread started");
    }

    @Override
    public void stop() {
        running = false;
        wakeUp();
        if (dispatchThread != null) {
            dispatchThread.interrupt();
            try {
                dispatchThread.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        executorService.shutdownNow();
        retryScheduler.shutdownNow();
        log.info("ApiFlowTaskScheduler stopped");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE - 1;
    }

    private void dispatchLoop() {
        log.info("Task dispatch loop entered");
        while (running) {
            try {
                boolean hasTask = pullAndDispatch();
                if (!hasTask) {
                    synchronized (wakeLock) {
                        try {
                            wakeLock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            if (!running) {
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error in dispatch loop", e);
                synchronized (wakeLock) {
                    try {
                        wakeLock.wait(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        log.info("Task dispatch loop exited");
    }

    @EventListener
    public void onTaskChange(TaskChangeEvent event) {
        wakeUp();
    }

    public void wakeUp() {
        synchronized (wakeLock) {
            wakeLock.notifyAll();
        }
    }

    private void scheduleWakeUp(long delayMs) {
        if (delayMs <= 0) {
            wakeUp();
            return;
        }
        retryScheduler.schedule(this::wakeUp, delayMs, TimeUnit.MILLISECONDS);
    }

    private boolean pullAndDispatch() {
        try {
            SelectTaskParam param = SelectTaskParam.builder()
                    .status(FieldCondition.of(TaskStatus.PENDING.getValue()))
                    .limit(SystemConstant.DEFAULT_BATCH_SIZE)
                    .build();

            List<TaskIDTO> tasks = taskRepository.selectList(param);
            if (tasks == null || tasks.isEmpty()) {
                return false;
            }

            boolean dispatched = false;
            long minRetryDelay = Long.MAX_VALUE;
            long now = System.currentTimeMillis();

            for (TaskIDTO taskDto : tasks) {
                if (taskDto.getNextRetryTimeMs() != null
                        && taskDto.getNextRetryTimeMs() > now) {
                    long delay = taskDto.getNextRetryTimeMs() - now;
                    if (delay < minRetryDelay) {
                        minRetryDelay = delay;
                    }
                    continue;
                }
                executorService.submit(() -> executeTask(taskDto));
                dispatched = true;
            }

            if (!dispatched && minRetryDelay < Long.MAX_VALUE) {
                scheduleWakeUp(minRetryDelay);
            }

            return dispatched;
        } catch (Exception e) {
            log.error("Error pulling tasks for execution", e);
            return false;
        }
    }

    public void executeTask(TaskIDTO taskDto) {
        String taskNo = taskDto.getTaskNo();
        if (!distributedLock.tryLock(taskNo, SystemConstant.DEFAULT_TASK_LOCK_EXPIRE_SECONDS)) {
            return;
        }

        localInterruptFlags.put(taskNo, new AtomicBoolean(false));

        try {
            UpdateTaskParam startParam = UpdateTaskParam.builder()
                    .id(taskDto.getId())
                    .status(TaskStatus.RUNNING.getValue())
                    .startExecuteTimeMs(System.currentTimeMillis())
                    .build();
            taskRepository.update(startParam);

            saveTaskLog(taskNo, "EXECUTE_START", Map.of("status", "RUNNING"));

            ExecInfo execInfo = executePluginChain(taskDto);

            UpdateTaskParam completeParam = UpdateTaskParam.builder()
                    .id(taskDto.getId())
                    .status(TaskStatus.SUCCESS.getValue())
                    .execInfo(null)
                    .responseData(JsonUtil.toJson(Map.of("result", "success")))
                    .endExecuteTimeMs(System.currentTimeMillis())
                    .build();
            taskRepository.update(completeParam);

            saveTaskLog(taskNo, "EXECUTE_SUCCESS", Map.of("status", "SUCCESS"));

            receiptService.sendReceipt(buildTaskDO(taskDto), null);

        } catch (BusinessException e) {
            handleTaskFailure(taskDto, e);
        } catch (Exception e) {
            handleTaskFailure(taskDto, e);
        } finally {
            distributedLock.unlock(taskNo);
            localInterruptFlags.remove(taskNo);
        }
    }

    private ExecInfo executePluginChain(TaskIDTO taskDto) {
        AtomicBoolean interruptFlag = localInterruptFlags.get(taskDto.getTaskNo());

        if (interruptFlag != null && interruptFlag.get()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED, "Task interrupted");
        }

        PluginContext context = PluginContext.of(
                taskDto.getTaskNo(),
                taskDto.getApiCode(),
                taskDto.getApiName(),
                null,
                null
        );

        List<PluginChainExecutor.PluginChainItemConfig> chainItems = List.of(
                new PluginChainExecutor.PluginChainItemConfig("PARAM_VALIDATOR", 1, true, null),
                new PluginChainExecutor.PluginChainItemConfig("RATE_LIMIT_CHECK", 2, true, null),
                new PluginChainExecutor.PluginChainItemConfig("BUSINESS_EXECUTOR", 3, true, null)
        );

        return pluginChainExecutor.execute(context, chainItems);
    }

    private void handleTaskFailure(TaskIDTO taskDto, Exception e) {
        String taskNo = taskDto.getTaskNo();
        log.error("Task execution failed: {}", taskNo, e);

        Integer retryCount = (taskDto.getRetryCount() != null ? taskDto.getRetryCount() : 0) + 1;
        Integer maxRetryCount = taskDto.getMaxRetryCount() != null ? taskDto.getMaxRetryCount() : SystemConstant.DEFAULT_AUTO_RETRY_COUNT;

        if (retryCount < maxRetryCount) {
            long baseInterval = SystemConstant.DEFAULT_RETRY_INTERVAL_MS;
            long nextRetryTime = System.currentTimeMillis() + baseInterval * (1L << Math.min(retryCount, 30));

            UpdateTaskParam retryParam = UpdateTaskParam.builder()
                    .id(taskDto.getId())
                    .status(TaskStatus.PENDING.getValue())
                    .retryCount(retryCount)
                    .nextRetryTimeMs(nextRetryTime)
                    .build();
            taskRepository.update(retryParam);
            saveTaskLog(taskNo, "EXECUTE_FAILED_RETRY", Map.of("retryCount", retryCount, "nextRetryTimeMs", nextRetryTime));

            long delayMs = nextRetryTime - System.currentTimeMillis();
            scheduleWakeUp(delayMs);
        } else {
            UpdateTaskParam failParam = UpdateTaskParam.builder()
                    .id(taskDto.getId())
                    .status(TaskStatus.FAILED.getValue())
                    .retryCount(retryCount)
                    .compensateStatus(CompensateStatus.PENDING.getValue())
                    .endExecuteTimeMs(System.currentTimeMillis())
                    .build();
            taskRepository.update(failParam);
            saveTaskLog(taskNo, "EXECUTE_FAILED", Map.of("retryCount", retryCount));
            alarmSender.send(AlarmEvent.taskFailed(taskNo, taskDto.getApiCode(), e.getMessage()));
        }
    }

    public void interruptTask(String taskNo) {
        AtomicBoolean flag = localInterruptFlags.get(taskNo);
        if (flag != null) {
            flag.set(true);
        }
    }

    private void saveTaskLog(String taskNo, String logType, Object logData) {
        try {
            SaveTaskLogParam param = SaveTaskLogParam.builder()
                    .taskNo(taskNo)
                    .logType(logType)
                    .logData(JsonUtil.toJson(logData))
                    .createTimeMs(System.currentTimeMillis())
                    .build();
            taskLogRepository.save(param);
        } catch (Exception e) {
            log.error("Failed to save task log: taskNo={}, logType={}", taskNo, logType, e);
        }
    }

    private TaskDO buildTaskDO(TaskIDTO dto) {
        return TaskDO.builder()
                .id(dto.getId())
                .taskNo(dto.getTaskNo())
                .apiCode(dto.getApiCode())
                .apiName(dto.getApiName())
                .status(dto.getStatus())
                .responseData(dto.getResponseData() != null ? dto.getResponseData().toString() : null)
                .createTimeMs(dto.getCreateTimeMs())
                .endExecuteTimeMs(dto.getEndExecuteTimeMs())
                .executeDurationMs(dto.getExecuteDurationMs())
                .build();
    }
}
