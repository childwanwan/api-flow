package com.apiflow.api.repository.task.idto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskReceiptConfigIDTO {
    private List<String> receiptTypes;
    private TaskHttpReceiptIDTO http;
    private TaskMqReceiptIDTO mq;
}
