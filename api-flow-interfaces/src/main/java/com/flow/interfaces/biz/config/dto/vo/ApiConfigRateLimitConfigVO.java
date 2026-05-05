package com.flow.interfaces.biz.config.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigRateLimitConfigVO {
    private Boolean enabled;
    private List<ApiConfigRateLimitRuleVO> rules;
}
