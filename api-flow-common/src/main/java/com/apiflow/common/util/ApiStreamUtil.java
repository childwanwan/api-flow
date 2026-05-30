package com.apiflow.common.util;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApiStreamUtil {

    /**
     * 提取属性，过滤空字符串（CharSequence），去重，返回 List
     */
    public static <T> List<String> mapFilterBlankDistinct(Collection<T> collection,
                                                          Function<? super T, ? extends CharSequence> mapper) {
        return of(collection)
                .map(mapper)
                .map(seq -> seq != null ? seq.toString() : null)
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 安全获取 Stream，自动处理 null 集合
     */
    public static <T> Stream<T> of(Collection<T> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

}
