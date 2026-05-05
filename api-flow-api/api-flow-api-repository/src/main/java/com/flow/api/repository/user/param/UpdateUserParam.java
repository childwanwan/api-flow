package com.flow.api.repository.user.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserParam {

    private Long id;
    private String password;
    private String role;
    private String status;
    private Long lastLoginTimeMs;

}