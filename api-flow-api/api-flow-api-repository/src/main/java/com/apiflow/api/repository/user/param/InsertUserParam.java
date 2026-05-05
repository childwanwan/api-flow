package com.apiflow.api.repository.user.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertUserParam {

    private String username;
    private String password;
    private String role;
    private String status;
    private String createOperator;

}