package com.apiflow.api.repository.operationlog.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBy {

    private OperationLogField field;

    private Boolean ascending;

    private Integer order;

    public static OrderBy asc(OperationLogField field) {
        return OrderBy.builder().field(field).ascending(true).build();
    }

    public static OrderBy desc(OperationLogField field) {
        return OrderBy.builder().field(field).ascending(false).build();
    }

    public static OrderBy asc(OperationLogField field, Integer order) {
        return OrderBy.builder().field(field).ascending(true).order(order).build();
    }

    public static OrderBy desc(OperationLogField field, Integer order) {
        return OrderBy.builder().field(field).ascending(false).order(order).build();
    }
}
