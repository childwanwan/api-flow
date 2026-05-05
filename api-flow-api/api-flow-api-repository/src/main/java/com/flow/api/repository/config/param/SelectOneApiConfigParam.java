package com.flow.api.repository.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectOneApiConfigParam {
    private String apiCode;
    private String groupNo;
    private Boolean enabled;
    private List<ApiConfigField> selectFields;
}
