-- ============================================
-- API接口网关中台 数据库初始化脚本
-- 版本: v3.0.0
-- 日期: 2026-05-03
-- ============================================

-- ============================================
-- 表1: API配置表
-- ============================================
DROP TABLE IF EXISTS api_config;
CREATE TABLE api_config (
    id                          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    group_no                    VARCHAR(64) COMMENT '分组编号',
    api_code                    VARCHAR(64) NOT NULL COMMENT 'API编码，唯一标识',
    api_name                    VARCHAR(128) NOT NULL COMMENT 'API名称',
    api_description             VARCHAR(512) COMMENT 'API描述',

    status                      VARCHAR(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用，DISABLED-禁用',

    request_timeout_ms          BIGINT NOT NULL DEFAULT 30000 COMMENT '请求超时时间(毫秒)',
    auto_retry_count            INT NOT NULL DEFAULT 64 COMMENT '最大重试次数',
    retry_interval_ms           BIGINT NOT NULL DEFAULT 5000 COMMENT '重试间隔(毫秒)',

    rate_limit_config           JSON COMMENT '限流规则配置JSON',
    max_queue_size              INT NOT NULL DEFAULT 100000 COMMENT '最大排队数量',

    filter_rules                JSON COMMENT '过滤规则JSON配置',
    plugin_config               JSON COMMENT '插件配置JSON（责任链配置）',
    receipt_config              JSON COMMENT '回执配置JSON',
    extra_config                JSON COMMENT '扩展配置JSON',

    create_time_ms              BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',
    update_time_ms              BIGINT NOT NULL COMMENT '更新时间戳(毫秒)',
    create_operator             VARCHAR(64) COMMENT '创建人',
    update_operator             VARCHAR(64) COMMENT '更新人',
    deleted                    TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',

    version                     INT NOT NULL DEFAULT 0 COMMENT '版本号',

    PRIMARY KEY (id),
    UNIQUE KEY uk_api_config_code_deleted (api_code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API配置表';

-- ============================================
-- 表2: 任务主表
-- ============================================
DROP TABLE IF EXISTS api_task;
CREATE TABLE api_task (
    id                              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    task_no                         VARCHAR(64) NOT NULL COMMENT '任务编号',
    source                          VARCHAR(64) NOT NULL DEFAULT 'API' COMMENT '操作来源：API-接口调用，SCHEDULE-定时任务，MANUAL-手动触发',
    group_no                        VARCHAR(64) COMMENT '任务批次编号（用于批量任务管理）',
    api_code                        VARCHAR(64) NOT NULL COMMENT 'API编码',
    api_name                        VARCHAR(128) COMMENT 'API名称(冗余)',

    action_type                     VARCHAR(32) NOT NULL DEFAULT 'ASYNC' COMMENT '动作类型：SYNC-同步执行（阻塞等待结果），ASYNC-异步执行（提交后立即返回）',

    status                          VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态：PENDING-待处理，RUNNING-执行中，SUCCESS-成功，FAILED-失败，CANCELED-已取消',

    interrupt_flag                  TINYINT NOT NULL DEFAULT 0 COMMENT '中断标志：0-未中断，1-已中断',
    compensate_status               VARCHAR(32) NOT NULL DEFAULT 'NONE' COMMENT '补偿状态：NONE-无需补偿，PENDING-待补偿，RUNNING-补偿中，SUCCESS-补偿成功，FAILED-补偿失败',
    compensate_retry_count          INT NOT NULL DEFAULT 0 COMMENT '补偿重试次数',
    compensate_next_retry_time_ms   BIGINT COMMENT '下次补偿重试时间戳(毫秒)',

    priority                        INT NOT NULL DEFAULT 10 COMMENT '优先级1-10，1最高',

    request_context                 JSON NOT NULL COMMENT '请求上下文JSON',
    exec_info                       JSON COMMENT '执行信息JSON(包含插件步骤列表)',
    response_data                   JSON COMMENT '响应数据JSON',
    expire_time_ms                  BIGINT COMMENT '任务过期时间戳(毫秒)，超过该时间任务将不再执行',

    receipt_config                  JSON COMMENT '回执配置JSON',
    receipt_info                    JSON COMMENT '回执执行记录JSON',

    retry_count                     INT NOT NULL DEFAULT 0 COMMENT '已重试次数',
    max_retry_count                 INT NOT NULL DEFAULT 64 COMMENT '最大重试次数',
    next_retry_time_ms              BIGINT COMMENT '下次重试时间戳(毫秒)',

    create_time_ms                  BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',
    start_execute_time_ms           BIGINT COMMENT '开始执行时间戳(毫秒)',
    end_execute_time_ms             BIGINT COMMENT '结束执行时间戳(毫秒)',
    execute_duration_ms             BIGINT COMMENT '执行耗时(毫秒)',

    cancel_time_ms                  BIGINT COMMENT '取消时间戳(毫秒)',
    cancel_reason                   VARCHAR(512) COMMENT '取消原因',
    canceled_by                     VARCHAR(64) COMMENT '取消人',

    update_time_ms                  BIGINT NOT NULL COMMENT '更新时间戳(毫秒)',
    deleted                        TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    version                         INT NOT NULL DEFAULT 0 COMMENT '版本号',

    PRIMARY KEY (id),
    UNIQUE KEY uk_api_task_no_deleted (task_no, deleted),
    INDEX idx_task_status_interrupt (status, interrupt_flag, priority, create_time_ms),
    INDEX idx_task_retry (status, next_retry_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务主表';

-- ============================================
-- 表3: 任务日志表
-- ============================================
DROP TABLE IF EXISTS api_task_log;
CREATE TABLE api_task_log (
    id                              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    task_no                         VARCHAR(64) NOT NULL COMMENT '任务编号',

    log_type                        VARCHAR(64) NOT NULL COMMENT '日志类型',

    log_data                        JSON NOT NULL COMMENT '日志内容JSON',

    create_time_ms                  BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',

    PRIMARY KEY (id),
    INDEX idx_task_log_task_no (task_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务日志表';

-- ============================================
-- 表4: 配置变更日志表
-- ============================================
DROP TABLE IF EXISTS api_config_log;
CREATE TABLE api_config_log (
    id                      BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    api_code                VARCHAR(64) NOT NULL COMMENT 'API编码',

    change_type             VARCHAR(32) NOT NULL COMMENT '变更类型：CREATE-创建，UPDATE-更新，DELETE-删除，ENABLE-启用，DISABLE-禁用',

    before_config           JSON COMMENT '变更前的完整配置',
    after_config            JSON COMMENT '变更后的完整配置',

    operator                VARCHAR(64) NOT NULL COMMENT '操作人',

    create_time_ms          BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',

    PRIMARY KEY (id),
    INDEX idx_config_log_api_code (api_code),
    INDEX idx_config_log_create_time (create_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='配置变更日志表';

-- ============================================
-- 表5: API分组表
-- ============================================
DROP TABLE IF EXISTS api_group;
CREATE TABLE api_group (
    id                      BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    group_no                VARCHAR(64) NOT NULL COMMENT '分组编号，内部不可变ID，用于连表',
    group_code              VARCHAR(64) NOT NULL COMMENT '分组编码，业务编码，可编辑',
    group_name              VARCHAR(128) NOT NULL COMMENT '分组名称',
    group_description       VARCHAR(512) COMMENT '分组描述',

    status                  VARCHAR(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用，DISABLED-禁用',

    create_time_ms          BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',
    update_time_ms          BIGINT NOT NULL COMMENT '更新时间戳(毫秒)',
    create_operator         VARCHAR(64) COMMENT '创建人',
    update_operator         VARCHAR(64) COMMENT '更新人',
    deleted                 TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',

    version                 INT NOT NULL DEFAULT 0 COMMENT '版本号',

    PRIMARY KEY (id),
    UNIQUE KEY uk_api_group_no_deleted (group_no, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API分组表';

-- ============================================
-- 表6: 用户表
-- ============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id                          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    username                    VARCHAR(64) NOT NULL COMMENT '用户名',
    password                    VARCHAR(256) NOT NULL COMMENT '密码(加密存储)',
    role                        VARCHAR(16) NOT NULL COMMENT '角色：ADMIN-管理员，USER-普通用户',
    status                      VARCHAR(16) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED-启用，DISABLED-禁用',

    create_time_ms              BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',
    update_time_ms              BIGINT NOT NULL COMMENT '更新时间戳(毫秒)',
    create_operator             VARCHAR(64) COMMENT '创建人',
    last_login_time_ms          BIGINT COMMENT '最后登录时间戳(毫秒)',

    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 表7: 操作日志表
-- ============================================
DROP TABLE IF EXISTS sys_operation_log;
CREATE TABLE sys_operation_log (
    id                      BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    operator                VARCHAR(64) NOT NULL COMMENT '操作人',
    operation_type          VARCHAR(32) NOT NULL COMMENT '操作类型',
    target_type             VARCHAR(64) COMMENT '操作对象类型',
    target_id               VARCHAR(64) COMMENT '操作对象ID',
    operation_detail        TEXT COMMENT '操作详情',

    operate_ip              VARCHAR(64) COMMENT '操作IP',
    create_time_ms          BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',

    PRIMARY KEY (id),
    INDEX idx_operation_log_operator (operator),
    INDEX idx_operation_log_create_time (create_time_ms)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ============================================
-- 表8: 插件配置表
-- ============================================
DROP TABLE IF EXISTS plugin_config;
CREATE TABLE plugin_config (
    id                          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',

    plugin_code                 VARCHAR(64) NOT NULL COMMENT '插件编码',
    plugin_name                 VARCHAR(128) NOT NULL COMMENT '插件名称',
    plugin_type                 VARCHAR(32) NOT NULL COMMENT '插件类型',
    class_name                  VARCHAR(256) NOT NULL COMMENT '插件类名',

    default_order               INT NOT NULL DEFAULT 1000 COMMENT '默认执行顺序',
    enabled                     TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',

    config_schema               JSON COMMENT '配置JSON Schema',
    description                 VARCHAR(512) COMMENT '插件描述',

    create_time_ms              BIGINT NOT NULL COMMENT '创建时间戳(毫秒)',
    update_time_ms              BIGINT NOT NULL COMMENT '更新时间戳(毫秒)',

    PRIMARY KEY (id),
    UNIQUE KEY uk_plugin_config_code (plugin_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='插件配置表';

-- ============================================
-- 种子数据
-- ============================================

-- 插入API配置
INSERT INTO api_config (
    api_code, api_name, api_description, status,
    request_timeout_ms, auto_retry_count, retry_interval_ms,
    rate_limit_config, max_queue_size,
    filter_rules, plugin_config, receipt_config,
    create_time_ms, update_time_ms, create_operator, deleted, version
) VALUES (
    'AMAZON_ORDER_QUERY',
    '亚马逊订单查询',
    '查询亚马逊平台订单信息',
    'ENABLED',
    30000, 64, 5000,
    '{"enabled":true,"rules":[{"name":"账号维度限流","type":"CONCURRENCY","dimension":"ACCOUNT","keyTemplate":"${params.accountId}","limit":100,"windowSeconds":1}]}',
    100000,
    '[{"field":"params.orderId","operator":"NOT_EMPTY","message":"订单号不能为空"},{"field":"params.marketplace","operator":"IN","value":["us-east-1","us-west-2"],"message":"不支持该市场"}]',
    '{"enabled":true,"pluginChain":[{"pluginCode":"API_EXECUTOR","order":1000,"enabled":true,"config":{}}]}',
    '{"receiptTypes":["MQ"],"mq":{"topic":"api-receipt","keyTemplate":"${taskNo}"}}',
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 0, 0
);

-- 10分钟执行一次的API示例
INSERT INTO api_config (
    api_code, api_name, api_description, status,
    request_timeout_ms, auto_retry_count, retry_interval_ms,
    rate_limit_config, max_queue_size,
    plugin_config,
    create_time_ms, update_time_ms, create_operator, deleted, version
) VALUES (
    'AMAZON_INVENTORY_SYNC',
    '亚马逊库存同步',
    '每10分钟同步一次亚马逊库存数据',
    'ENABLED',
    60000, 64, 10000,
    '{"enabled":true,"rules":[{"name":"账号维度限流","type":"CONCURRENCY","dimension":"ACCOUNT","keyTemplate":"${params.accountId}","limit":50,"windowSeconds":1}]}',
    100,
    '{"enabled":true,"pluginChain":[{"pluginCode":"API_EXECUTOR","order":1000,"enabled":true,"config":{}}]}',
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 0, 0
);

-- 每小时执行一次的API示例
INSERT INTO api_config (
    api_code, api_name, api_description, status,
    request_timeout_ms, auto_retry_count, retry_interval_ms,
    rate_limit_config, max_queue_size,
    plugin_config,
    create_time_ms, update_time_ms, create_operator, deleted, version
) VALUES (
    'WALMART_ORDER_QUERY',
    '沃尔玛订单查询',
    '每小时执行一次订单查询',
    'ENABLED',
    30000, 64, 5000,
    '{"enabled":true,"rules":[{"name":"账号维度限流","type":"CONCURRENCY","dimension":"ACCOUNT","keyTemplate":"${params.accountId}","limit":100,"windowSeconds":1}]}',
    500,
    '{"enabled":true,"pluginChain":[{"pluginCode":"API_EXECUTOR","order":1000,"enabled":true,"config":{}}]}',
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 0, 0
);

-- 每天执行一次的API示例
INSERT INTO api_config (
    api_code, api_name, api_description, status,
    request_timeout_ms, auto_retry_count, retry_interval_ms,
    rate_limit_config, max_queue_size,
    plugin_config,
    create_time_ms, update_time_ms, create_operator, deleted, version
) VALUES (
    'DAILY_REPORT_GENERATE',
    '日报生成',
    '每天凌晨2点生成报表',
    'ENABLED',
    120000, 64, 30000,
    '{"enabled":false}',
    10,
    '{"enabled":true,"pluginChain":[{"pluginCode":"API_EXECUTOR","order":1000,"enabled":true,"config":{}}]}',
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 0, 0
);

-- QPS限流示例
INSERT INTO api_config (
    api_code, api_name, api_description, status,
    request_timeout_ms, auto_retry_count, retry_interval_ms,
    rate_limit_config, max_queue_size,
    filter_rules, plugin_config, receipt_config,
    create_time_ms, update_time_ms, create_operator, deleted, version
) VALUES (
    'AMAZON_LABEL_DOWNLOAD',
    '亚马逊面单下载',
    '下载亚马逊平台面单文件',
    'ENABLED',
    60000, 64, 10000,
    '{"enabled":true,"rules":[{"name":"API维度限流","type":"QPS","dimension":"API","keyTemplate":"${externalApiCode}","limit":50,"windowSeconds":1}]}',
    500,
    '[{"field":"params.orderId","operator":"MATCHES","value":"^AMZ[0-9]{10,12}$","message":"订单号格式不正确"}]',
    '{"enabled":true,"pluginChain":[{"pluginCode":"API_EXECUTOR","order":1000,"enabled":true,"config":{}}]}',
    '{"receiptTypes":["HTTP"],"http":{"url":"https://api.amazon.com/labels/${orderId}","method":"GET","timeoutMs":60000}}',
    UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000, 'system', 0, 0
);
