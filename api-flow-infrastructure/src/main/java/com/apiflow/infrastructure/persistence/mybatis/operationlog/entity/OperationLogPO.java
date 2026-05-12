package com.apiflow.infrastructure.persistence.mybatis.operationlog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_operation_log")
public class OperationLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("biz_code")
    private String bizCode;

    @TableField("log_type")
    private String logType;

    @TableField("log_data")
    private String logData;

    @TableField("operator")
    private String operator;

    @TableField("operate_time_ms")
    private Long operateTimeMs;

    @TableField("create_time_ms")
    private Long createTimeMs;
}
