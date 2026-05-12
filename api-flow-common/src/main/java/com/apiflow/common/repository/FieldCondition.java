package com.apiflow.common.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldCondition<T> {

    private T eq;
    private T ne;
    private T gt;
    private T ge;
    private T lt;
    private T le;
    private String like;
    private String likeLeft;
    private String likeRight;
    private List<T> in;
    private List<T> notIn;
    private T betweenStart;
    private T betweenEnd;
    private Boolean isNull;
    private Boolean isNotNull;

    public static <T> FieldCondition<T> of(T value) {
        return FieldCondition.<T>builder().eq(value).build();
    }

    public boolean hasAnyCondition() {
        return eq != null || ne != null || gt != null || ge != null
                || lt != null || le != null || like != null || likeLeft != null || likeRight != null
                || (in != null && !in.isEmpty()) || (notIn != null && !notIn.isEmpty())
                || betweenStart != null || betweenEnd != null
                || Boolean.TRUE.equals(isNull) || Boolean.TRUE.equals(isNotNull);
    }
}
