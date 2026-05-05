package com.apiflow.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraConfigDTO {

    private String region;
    private String sellerId;
    private String awsAccessKey;
    private String environment;
}
