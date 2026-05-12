package com.apiflow.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;
    
    private long total;
    
    private long size;
    
    private long current;
    
    private long pages;

    public static <T> PageResult<T> empty() {
        return PageResult.<T>builder()
                .records(Collections.emptyList())
                .total(0)
                .size(0)
                .current(1)
                .pages(0)
                .build();
    }
    
    public static <T> PageResult<T> of(List<T> records, long total, long current, long size) {
        long pages = size > 0 ? (total + size - 1) / size : 0;
        return PageResult.<T>builder()
                .records(records)
                .total(total)
                .size(size)
                .current(current)
                .pages(pages)
                .build();
    }
    
    public boolean hasNext() {
        return current < pages;
    }
    
    public boolean hasPrevious() {
        return current > 1;
    }
}
