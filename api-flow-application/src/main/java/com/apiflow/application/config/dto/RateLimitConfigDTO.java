package com.apiflow.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateLimitConfigDTO {

    private Boolean enabled;
    private List<RateLimitRuleDTO> rules;
}
