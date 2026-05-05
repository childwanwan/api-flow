-- 创建数据库
CREATE DATABASE IF NOT EXISTS api_flow
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE api_flow;

-- API配置表
DROP TABLE IF EXISTS api_config;
CREATE TABLE api_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    group_no VARCHAR(64) COMMENT '分组编号',
    api_code VARCHAR(64) NOT NULL COMMENT 'API编码，唯一标识',
    api_name VARCHAR(128) NOT NULL COMMENT 'API名称',
    api_description VARCHAR(512) COMMENT 'API描述',
    status VARCHAR(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用，DISABLED-禁用',
    request_timeout_ms BIGINT NOT NULL DEFAULT 30000 COMMENT '请求超时时间（毫秒）',
    auto_retry_count INT NOT NULL DEFAULT 64 COMMENT '最大重试次数',
    retry_interval_ms BIGINT NOT NULL DEFAULT 5000 COMMENT '重试间隔（毫秒）',
    rate_limit_config JSON COMMENT '限流规则配置JSON',
    max_queue_size INT NOT NULL DEFAULT 100000 COMMENT '最大队列长度',
    filter_rules JSON COMMENT '过滤规则JSON配置',
    plugin_config JSON COMMENT '插件配置JSON（责任链配置）',
    receipt_config JSON COMMENT '回执配置JSON',
    extra_config JSON COMMENT '扩展配置JSON',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time_ms BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    create_operator VARCHAR(64) COMMENT '创建人',
    update_operator VARCHAR(64) COMMENT '更新人',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_api_config_code_deleted (api_code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API配置表';

-- 任务主表
DROP TABLE IF EXISTS api_task;
CREATE TABLE api_task (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    task_no VARCHAR(64) NOT NULL COMMENT '任务编号',
    source VARCHAR(64) NOT NULL DEFAULT 'API' COMMENT '操作来源：API-接口调用，SCHEDULE-定时任务，MANUAL-手动触发',
    group_no VARCHAR(64) COMMENT '任务批次编号（用于批量任务管理）',
    api_code VARCHAR(64) NOT NULL COMMENT 'API编码',
    api_name VARCHAR(128) COMMENT 'API名称（冗余）',
    action_type VARCHAR(32) NOT NULL DEFAULT 'ASYNC' COMMENT '动作类型：SYNC-同步执行（阻塞等待结果），ASYNC-异步执行（提交后立即返回）',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态：PENDING-待处理，RUNNING-执行中，SUCCESS-成功，FAILED-失败，CANCELED-已取消',
    interrupt_flag TINYINT NOT NULL DEFAULT 0 COMMENT '中断标志：0-未中断，1-已中断',
    compensate_status VARCHAR(32) NOT NULL DEFAULT 'NONE' COMMENT '补偿状态：NONE-无需补偿，PENDING-待补偿，RUNNING-补偿中，SUCCESS-补偿成功，FAILED-补偿失败',
    compensate_retry_count INT NOT NULL DEFAULT 0 COMMENT '补偿重试次数',
    compensate_next_retry_time_ms BIGINT COMMENT '下次补偿重试时间戳（毫秒）',
    priority INT NOT NULL DEFAULT 10 COMMENT '优先级1-10',
    request_context JSON NOT NULL COMMENT '请求上下文JSON',
    exec_info JSON COMMENT '执行信息JSON（包含插件步骤列表）',
    response_data JSON COMMENT '响应数据JSON',
    expire_time_ms BIGINT COMMENT '任务过期时间戳（毫秒），超过该时间任务将不再执行',
    receipt_config JSON COMMENT '回执配置JSON',
    receipt_info JSON COMMENT '回执执行记录JSON',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '已重试次数',
    max_retry_count INT NOT NULL DEFAULT 64 COMMENT '最大重试次数',
    next_retry_time_ms BIGINT COMMENT '下次重试时间戳（毫秒）',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    start_execute_time_ms BIGINT COMMENT '开始执行时间戳（毫秒）',
    end_execute_time_ms BIGINT COMMENT '结束执行时间戳（毫秒）',
    execute_duration_ms BIGINT COMMENT '执行耗时（毫秒）',
    cancel_time_ms BIGINT COMMENT '取消时间戳（毫秒）',
    cancel_reason VARCHAR(512) COMMENT '取消原因',
    canceled_by VARCHAR(64) COMMENT '取消人',
    update_time_ms BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_api_task_no_deleted (task_no, deleted),
    INDEX idx_task_status_interrupt (status, interrupt_flag, priority, create_time_ms),
    INDEX idx_task_retry (status, next_retry_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务主表';

-- 任务日志表
DROP TABLE IF EXISTS api_task_log;
CREATE TABLE api_task_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    task_no VARCHAR(64) NOT NULL COMMENT '任务编号',
    log_type VARCHAR(64) NOT NULL COMMENT '日志类型',
    log_data JSON NOT NULL COMMENT '日志内容JSON',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    PRIMARY KEY (id),
    INDEX idx_task_log_task_no (task_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务日志表';

-- API分组表
DROP TABLE IF EXISTS api_group;
CREATE TABLE api_group (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    group_no VARCHAR(64) NOT NULL COMMENT '分组编号',
    group_name VARCHAR(128) NOT NULL COMMENT '分组名称',
    group_description VARCHAR(512) COMMENT '分组描述',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time_ms BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    create_operator VARCHAR(64) COMMENT '创建人',
    update_operator VARCHAR(64) COMMENT '更新人',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_api_group_no_deleted (group_no, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API分组表';

-- 配置变更日志表
DROP TABLE IF EXISTS api_config_log;
CREATE TABLE api_config_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    api_code VARCHAR(64) NOT NULL COMMENT 'API编码',
    change_type VARCHAR(64) NOT NULL COMMENT '变更类型：CREATE/UPDATE/ENABLE/DISABLE/DELETE',
    before_config JSON COMMENT '变更前配置JSON',
    after_config JSON COMMENT '变更后配置JSON',
    operator VARCHAR(64) COMMENT '操作人',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    PRIMARY KEY (id),
    INDEX idx_config_log_api_code (api_code),
    INDEX idx_config_log_create_time (create_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配置变更日志表';

-- 操作日志表
DROP TABLE IF EXISTS sys_operation_log;
CREATE TABLE sys_operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(64) COMMENT '操作用户',
    operation VARCHAR(128) COMMENT '操作类型',
    module VARCHAR(64) COMMENT '操作模块',
    detail TEXT COMMENT '操作详情',
    ip VARCHAR(64) COMMENT 'IP地址',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    PRIMARY KEY (id),
    INDEX idx_operation_log_username (username),
    INDEX idx_operation_log_create_time (create_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 插件配置表
DROP TABLE IF EXISTS plugin_config;
CREATE TABLE plugin_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    plugin_code VARCHAR(64) NOT NULL COMMENT '插件编码',
    plugin_name VARCHAR(128) NOT NULL COMMENT '插件名称',
    plugin_class VARCHAR(256) NOT NULL COMMENT '插件实现类',
    description VARCHAR(512) COMMENT '插件描述',
    config JSON COMMENT '插件自定义配置JSON',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    order_num INT NOT NULL DEFAULT 0 COMMENT '排序号',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time_ms BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (id),
    UNIQUE KEY uk_plugin_config_code_deleted (plugin_code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='插件配置表';

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码（MD5加密）',
    role VARCHAR(32) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
    status VARCHAR(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用，DISABLED-禁用',
    create_time_ms BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time_ms BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    create_operator VARCHAR(64) COMMENT '创建人',
    last_login_time_ms BIGINT COMMENT '最后登录时间戳（毫秒）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 初始管理员用户（密码: admin123 的MD5）
INSERT INTO sys_user (username, password, role, status, create_time_ms, update_time_ms, create_operator)
VALUES ('admin', '0192023a7bbd73250516f069df18b500', 'ADMIN', 'ENABLED', 1746153600000, 1746153600000, 'system');

-- 初始API分组
INSERT INTO api_group (group_no, group_name, group_description, create_time_ms, update_time_ms, create_operator)
VALUES ('DEFAULT', '默认分组', '系统默认分组', 1746153600000, 1746153600000, 'system');

-- 初始插件配置
INSERT INTO plugin_config (plugin_code, plugin_name, plugin_class, description, enabled, order_num, create_time_ms, update_time_ms)
VALUES ('PARAM_VALIDATOR', '参数校验插件', 'com.apiflow.domain.plugin.builtin.ParamValidatorPlugin', '校验请求参数是否合法', 1, 1, 1746153600000, 1746153600000);

INSERT INTO plugin_config (plugin_code, plugin_name, plugin_class, description, enabled, order_num, create_time_ms, update_time_ms)
VALUES ('RATE_LIMIT_CHECK', '限流检查插件', 'com.apiflow.domain.plugin.builtin.RateLimitCheckPlugin', '检查是否触发限流规则', 1, 2, 1746153600000, 1746153600000);

INSERT INTO plugin_config (plugin_code, plugin_name, plugin_class, description, enabled, order_num, create_time_ms, update_time_ms)
VALUES ('BUSINESS_EXECUTOR', '业务执行插件', 'com.apiflow.domain.plugin.builtin.BusinessExecutorPlugin', '执行核心业务逻辑', 1, 3, 1746153600000, 1746153600000);
