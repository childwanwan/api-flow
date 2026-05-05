package com.flow.api.repository.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIDTO {

    private Long id;
    private String username;
    private String password;
    private String role;
    private String status;
    private Long createTimeMs;
    private Long updateTimeMs;
    private String createOperator;
    private Long lastLoginTimeMs;

}
