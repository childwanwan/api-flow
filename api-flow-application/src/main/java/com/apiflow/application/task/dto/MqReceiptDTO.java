package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqReceiptDTO {

    private String topic;
    private String keyTemplate;
    private Map<String, String> headers;
    private Object bodyTemplate;
}
