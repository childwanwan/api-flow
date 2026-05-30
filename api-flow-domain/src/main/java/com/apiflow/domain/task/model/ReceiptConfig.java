package com.apiflow.domain.task.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class ReceiptConfig {

    private final List<String> receiptTypes;
    private final HttpReceipt http;
    private final MqReceipt mq;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptConfig other)) return false;
        return ObjectUtil.equal(receiptTypes, other.receiptTypes)
                && ObjectUtil.equal(http, other.http)
                && ObjectUtil.equal(mq, other.mq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptTypes, http, mq);
    }
}
