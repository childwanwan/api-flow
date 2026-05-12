package com.apiflow.api.repository.group.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBy {
    
    private ApiGroupField field;
    
    private Boolean ascending;
    
    private Integer order;
    
    public static OrderBy asc(ApiGroupField field) {
        return OrderBy.builder().field(field).ascending(true).build();
    }
    
    public static OrderBy desc(ApiGroupField field) {
        return OrderBy.builder().field(field).ascending(false).build();
    }
    
    public static OrderBy asc(ApiGroupField field, Integer order) {
        return OrderBy.builder().field(field).ascending(true).order(order).build();
    }
    
    public static OrderBy desc(ApiGroupField field, Integer order) {
        return OrderBy.builder().field(field).ascending(false).order(order).build();
    }
}
