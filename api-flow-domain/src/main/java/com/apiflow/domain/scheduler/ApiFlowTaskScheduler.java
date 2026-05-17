package com.apiflow.domain.scheduler;

import com.apiflow.api.repository.config.ApiConfigRepository;
import com.apiflow.api.repository.config.idto.ApiConfigIDTO;
import com.apiflow.api.repository.config.param.SelectOneApiConfigParam;
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
import com.apiflow.common.util.TraceContext;
import com.apiflow.domain.alarm.AlarmEvent;
import com.apiflow.domain.alarm.AlarmSender;
import com.apiflow.domain.config.converter.ApiConfigConverter;
import com.apiflow.domain.config.model.ApiConfig;
import com.apiflow.domain.config.model.ExtraConfig;
import com.apiflow.domain.config.model.PluginChainItem;
import com.apiflow.domain.config.model.PluginConfig;
import com.apiflow.domain.lock.DistributedLock;
import com.apiflow.domain.plugin.PluginChainExecutor;
import com.apiflow.domain.plugin.PluginContext;
import com.apiflow.domain.receipt.ReceiptService;
import com.apiflow.domain.task.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ApiFlowTaskScheduler {

    private static final long LEADER_CHECK_INTERVAL_MS = 5000;
    private static final long SIGNAL_CHECK_INTERVAL_MS = 2000;
    private static final ApiConfigConverter API_CONFIG_CONVERTER = ApiConfigConverter.INSTANCE;

    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;
    private final ApiConfigRepository apiConfigRepository;
    private final DistributedLock distributedLock;
    private final PluginChainExecutor pluginChainExecutor;
    private final ReceiptService receiptService;
    private final AlarmSender alarmSender;
    private final SchedulerLeaderElection leaderElection;
    private final TaskWakeUpSignal wakeUpSignal;

    private final ConcurrentMap<String, AtomicBoolean> localInterruptFlags = new ConcurrentHashMap<>();
    private final ExecutorService taskExecutor;
    private final ScheduledExecutorService retryScheduler;
    private final ScheduledExecutorService leaderRenewScheduler;
    private final ScheduledExecutorService signalCheckScheduler;

    public ApiFlowTaskScheduler(
            TaskRepository taskRepository,
            TaskLogRepository taskLogRepository,
            ApiConfigRepository apiConfigRepository,
            DistributedLock distributedLock,
            PluginChainExecutor pluginChainExecutor,
            ReceiptService receiptService,
            AlarmSender alarmSender,
            SchedulerLeaderElection leaderElection,
            TaskWakeUpSignal wakeUpSignal,
            ExecutorService taskExecutor,
            ScheduledExecutorService retryScheduler,
            ScheduledExecutorService leaderRenewScheduler,
            ScheduledExecutorService signalCheckScheduler) {
        this.taskRepository = taskRepository;
        this.taskLogRepository = taskLogRepository;
        this.apiConfigRepository = apiConfigRepository;
        this.distributedLock = distributedLock;
        this.pluginChainExecutor = pluginChainExecutor;
        this.receiptService = receiptService;
        this.alarmSender = alarmSender;
        this.leaderElection = leaderElection;
        this.wakeUpSignal = wakeUpSignal;
        this.taskExecutor = taskExecutor;
        this.retryScheduler = retryScheduler;
        this.leaderRenewScheduler = leaderRenewScheduler;
        this.signalCheckScheduler = signalCheckScheduler;
    }

    private final Object wakeLock = new Object();
    private volatile boolean running = false;
    private Thread dispatchThread;

    public void start() {
        if (running) {
            return;
        }
        running = true;
        leaderElection.tryAcquireLeadership();
        leaderRenewScheduler.scheduleAtFixedRate(this::renewOrAcquireLeadership,
                SIGNAL_CHECK_INTERVAL_MS, SIGNAL_CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);
        signalCheckScheduler.scheduleAtFixedRate(this::checkWakeUpSignal,
                SIGNAL_CHECK_INTERVAL_MS, SIGNAL_CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);
        dispatchThread = new Thread(this::dispatchLoop, "task-dispatcher");
        dispatchThread.setDaemon(true);
        dispatchThread.start();
        log.info("ApiFlowTaskScheduler dispatch thread started, node=[{}]", leaderElection.getInstanceId());
    }

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
        leaderRenewScheduler.shutdownNow();
        signalCheckScheduler.shutdownNow();
        leaderElection.relinquishLeadership();
        taskExecutor.shutdownNow();
        retryScheduler.shutdownNow();
        log.info("ApiFlowTaskScheduler stopped");
    }

    public boolean isRunning() {
        return running;
    }

    private void renewOrAcquireLeadership() {
        if (leaderElection.isLeader()) {
            boolean renewed = leaderElection.renewLeadership();
            if (!renewed) {
                log.warn("Node [{}] lost leadership during renewal, will re-try acquire", leaderElection.getInstanceId());
                wakeUp();
            }
        } else {
            leaderElection.tryAcquireLeadership();
            if (leaderElection.isLeader()) {
                wakeUp();
            }
        }
    }

    private void checkWakeUpSignal() {
        if (leaderElection.isLeader() && wakeUpSignal.checkAndConsume()) {
            wakeUp();
        }
    }

    private void dispatchLoop() {
        log.info("Task dispatch loop entered, node=[{}]", leaderElection.getInstanceId());
        while (running) {
            try {
                if (!leaderElection.isLeader()) {
                    synchronized (wakeLock) {
                        try {
                            wakeLock.wait(LEADER_CHECK_INTERVAL_MS);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            if (!running) {
                                break;
                            }
                        }
                    }
                    continue;
                }

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
                taskExecutor.submit(TraceContext.wrap(() -> executeTask(taskDto)));
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
        TraceContext.setTraceId(taskNo);

        if (!distributedLock.tryLock(taskNo, SystemConstant.DEFAULT_TASK_LOCK_EXPIRE_SECONDS)) {
            TraceContext.clear();
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
            TraceContext.clear();
        }
    }

    private ExecInfo executePluginChain(TaskIDTO taskDto) {
        AtomicBoolean interruptFlag = localInterruptFlags.get(taskDto.getTaskNo());

        if (interruptFlag != null && interruptFlag.get()) {
            throw new BusinessException(ErrorCode.TASK_STATUS_NOT_ALLOWED, "Task interrupted");
        }

        ApiConfig config = null;
        try {
            SelectOneApiConfigParam configParam = SelectOneApiConfigParam.builder()
                    .apiCode(FieldCondition.of(taskDto.getApiCode())).build();
            ApiConfigIDTO configDto = apiConfigRepository.selectOne(configParam);
            if (configDto != null && !Boolean.TRUE.equals(configDto.getDeleted())) {
                config = API_CONFIG_CONVERTER.apiConfigIDTOToApiConfig(configDto);
            }
        } catch (Exception e) {
            log.warn("Failed to load ApiConfig for apiCode={}, using defaults", taskDto.getApiCode(), e);
        }

        ExtraConfig extraConfig = config != null ? config.getExtraConfig() : null;

        PluginContext context = PluginContext.of(
                taskDto.getTaskNo(),
                taskDto.getApiCode(),
                taskDto.getApiName(),
                null,
                null,
                extraConfig
        );

        List<PluginChainExecutor.PluginChainItemConfig> chainItems = buildChainItems(config);

        return pluginChainExecutor.execute(context, chainItems);
    }

    private List<PluginChainExecutor.PluginChainItemConfig> buildChainItems(ApiConfig config) {
        if (config != null && config.getPluginConfig() != null
                && Boolean.TRUE.equals(config.getPluginConfig().getEnabled())
                && config.getPluginConfig().getPluginChain() != null) {
            return config.getPluginConfig().getPluginChain().stream()
                    .map(item -> new PluginChainExecutor.PluginChainItemConfig(
                            item.getPluginCode(), item.getOrder(),
                            Boolean.TRUE.equals(item.getEnabled()), item.getConfig()))
                    .toList();
        }
        return List.of(
                new PluginChainExecutor.PluginChainItemConfig("PARAM_VALIDATOR", 1, true, null),
                new PluginChainExecutor.PluginChainItemConfig("RATE_LIMIT_CHECK", 2, true, null),
                new PluginChainExecutor.PluginChainItemConfig("BUSINESS_EXECUTOR", 3, true, null)
        );
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
