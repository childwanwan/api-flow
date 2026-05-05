package com.apiflow.interfaces.biz.config.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigFilterRulesVO {
    private List<ApiConfigFilterRuleVO> rules;
}
