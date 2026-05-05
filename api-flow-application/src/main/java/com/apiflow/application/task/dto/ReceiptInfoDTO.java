package com.apiflow.application.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptInfoDTO {

    private HttpReceiptRecordDTO http;
    private MqReceiptRecordDTO mq;
}
