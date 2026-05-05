package com.apiflow.domain.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptInfo {

    private HttpReceiptRecord http;
    private MqReceiptRecord mq;
}
