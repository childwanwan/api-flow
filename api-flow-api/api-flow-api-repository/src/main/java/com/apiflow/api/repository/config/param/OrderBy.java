package com.apiflow.api.repository.config.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBy {

    private ApiConfigField field;

    private Boolean ascending;

    private Integer order;

    public static OrderBy asc(ApiConfigField field) {
        return OrderBy.builder().field(field).ascending(true).build();
    }

    public static OrderBy desc(ApiConfigField field) {
        return OrderBy.builder().field(field).ascending(false).build();
    }

    public static OrderBy asc(ApiConfigField field, Integer order) {
        return OrderBy.builder().field(field).ascending(true).order(order).build();
    }

    public static OrderBy desc(ApiConfigField field, Integer order) {
        return OrderBy.builder().field(field).ascending(false).order(order).build();
    }
}
