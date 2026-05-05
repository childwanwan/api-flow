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
public class UpdateTaskMqReceiptRecordParam {
    private List<UpdateTaskReceiptAttemptParam> attempts;
}
