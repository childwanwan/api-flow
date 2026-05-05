package com.apiflow.api.repository.task.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReceiptConfigParam {
    private List<String> receiptTypes;
    private TaskHttpReceiptParam http;
    private TaskMqReceiptParam mq;
}
