package com.flow.api.repository.user.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOneUserParam {
    private String username;

    public boolean isEmpty() {
        if (StringUtils.isNotEmpty(username)) {
            return false;
        }
        return true;
    }
}
