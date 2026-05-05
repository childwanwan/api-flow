package com.flow.infrastructure.persistence.mybatis.user.entity;

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
@TableName("sys_user")
public class UserPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("status")
    private String status;

    @TableField("create_time_ms")
    private Long createTimeMs;

    @TableField("update_time_ms")
    private Long updateTimeMs;

    @TableField("create_operator")
    private String createOperator;

    @TableField("last_login_time_ms")
    private Long lastLoginTimeMs;

}