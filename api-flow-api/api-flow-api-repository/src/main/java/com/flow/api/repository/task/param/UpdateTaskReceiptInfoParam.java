package com.flow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskReceiptInfoParam {
    private UpdateTaskHttpReceiptRecordParam http;
    private UpdateTaskMqReceiptRecordParam mq;
}
