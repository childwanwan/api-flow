package com.apiflow.common.dto;

import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasePageParam {
    public final static Long DEFAULT_CURRENT = 1L;
    public final static Long DEFAULT_SIZE = 20L;


    private Long current;

    private Long size;

    private List<SortOrder> sortOrderList;


    public void validateBasePageParam() {
        if (ObjectUtils.isEmpty(current)
                || ObjectUtils.isEmpty(size)) {
            throw new BusinessException(ErrorCode.PAGE_PARAM_IS_EMPTY);
        }
        if (size > 1000) {
            throw new BusinessException(ErrorCode.PAGE_SIZE_TOO_LARGE);
        }
    }


    public Long calculateOffset() {
        long current = getEffectiveCurrent();
        long size = getEffectiveSize();
        return (current - 1) * size;
    }

    public Long getEffectiveSize() {
        return ObjectUtils.isNotEmpty(this.getSize()) ? this.getSize() : DEFAULT_SIZE;
    }

    public Long getEffectiveCurrent() {
        return ObjectUtils.isNotEmpty(this.getCurrent()) ? this.getCurrent() : DEFAULT_CURRENT;
    }
}
