package com.apiflow.api.repository.config.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfigFilterRulesIDTO {
    private List<ApiConfigFilterRuleIDTO> rules;
}
