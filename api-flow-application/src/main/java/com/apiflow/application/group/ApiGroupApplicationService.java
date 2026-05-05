package com.apiflow.application.group;

import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.SelectApiGroupParam;
import com.apiflow.api.repository.group.param.SelectOneApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;
import com.apiflow.common.exception.BusinessException;
import com.apiflow.common.exception.ErrorCode;
import com.apiflow.common.repository.FieldCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiGroupApplicationService {

    private final ApiGroupRepository apiGroupRepository;

    public ApiGroupIDTO createGroup(String groupNo, String groupName, String groupDescription, String operator) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .groupNo(FieldCondition.of(groupNo)).build();
        ApiGroupIDTO existing = apiGroupRepository.selectOne(param);
        if (existing != null && !Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.PARAM_VALIDATION_FAILED, "分组编号已存在");
        }

        long now = System.currentTimeMillis();
        SaveApiGroupParam saveParam = SaveApiGroupParam.builder()
                .groupNo(groupNo)
                .groupName(groupName)
                .groupDescription(groupDescription)
                .createTimeMs(now)
                .updateTimeMs(now)
                .createOperator(operator)
                .updateOperator(operator)
                .build();
        return apiGroupRepository.save(saveParam);
    }

    public ApiGroupIDTO updateGroup(String groupNo, String groupName, String groupDescription, String operator) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .groupNo(FieldCondition.of(groupNo)).build();
        ApiGroupIDTO existing = apiGroupRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND, "分组不存在");
        }

        UpdateApiGroupParam updateParam = UpdateApiGroupParam.builder()
                .id(existing.getId())
                .groupNo(groupNo)
                .groupName(groupName)
                .groupDescription(groupDescription)
                .updateTimeMs(System.currentTimeMillis())
                .updateOperator(operator)
                .build();
        return apiGroupRepository.update(updateParam);
    }

    public ApiGroupIDTO getGroup(String groupNo) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .groupNo(FieldCondition.of(groupNo)).build();
        ApiGroupIDTO group = apiGroupRepository.selectOne(param);
        if (group == null || Boolean.TRUE.equals(group.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND, "分组不存在");
        }
        return group;
    }

    public List<ApiGroupIDTO> listGroups(String groupNo, String groupName) {
        SelectApiGroupParam param = SelectApiGroupParam.builder()
                .groupNo(groupNo != null ? FieldCondition.<String>builder().like(groupNo).build() : null)
                .groupName(groupName != null ? FieldCondition.<String>builder().like(groupName).build() : null)
                .build();
        return apiGroupRepository.selectList(param);
    }

    public void deleteGroup(String groupNo, String operator) {
        SelectOneApiGroupParam param = SelectOneApiGroupParam.builder()
                .groupNo(FieldCondition.of(groupNo)).build();
        ApiGroupIDTO existing = apiGroupRepository.selectOne(param);
        if (existing == null || Boolean.TRUE.equals(existing.getDeleted())) {
            throw new BusinessException(ErrorCode.API_NOT_FOUND, "分组不存在");
        }

        UpdateApiGroupParam updateParam = UpdateApiGroupParam.builder()
                .id(existing.getId())
                .updateTimeMs(System.currentTimeMillis())
                .updateOperator(operator)
                .build();
        apiGroupRepository.update(updateParam);
    }
}
