package com.apiflow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveTaskReceiptInfoParam {
    private SaveTaskHttpReceiptRecordParam http;
    private SaveTaskMqReceiptRecordParam mq;
}
