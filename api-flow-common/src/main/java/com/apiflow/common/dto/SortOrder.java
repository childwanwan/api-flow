package com.apiflow.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortOrder {

    private String field;

    private Boolean ascending;

    private Integer order;

    public static SortOrder asc(String field) {
        return SortOrder.builder().field(field).ascending(true).build();
    }

    public static SortOrder desc(String field) {
        return SortOrder.builder().field(field).ascending(false).build();
    }

    public static SortOrder asc(String field, Integer order) {
        return SortOrder.builder().field(field).ascending(true).order(order).build();
    }

    public static SortOrder desc(String field, Integer order) {
        return SortOrder.builder().field(field).ascending(false).order(order).build();
    }
}
