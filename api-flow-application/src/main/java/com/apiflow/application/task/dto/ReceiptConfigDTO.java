package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptConfigDTO {

    private List<String> receiptTypes;
    private HttpReceiptDTO http;
    private MqReceiptDTO mq;
}
