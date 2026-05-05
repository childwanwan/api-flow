package com.flow.api.repository.user.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectUserParam {

    private String username;
    private String role;
    private String status;

    public boolean isEmpty() {
        return (username == null && role == null && status == null);
    }

}