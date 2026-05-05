package com.apiflow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReceiptInfoIDTO {
    private TaskHttpReceiptRecordIDTO http;
    private TaskMqReceiptRecordIDTO mq;
}
