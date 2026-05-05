package com.apiflow.infrastructure.persistence.mybatis.group;

import com.apiflow.api.repository.group.ApiGroupRepository;
import com.apiflow.api.repository.group.idto.ApiGroupIDTO;
import com.apiflow.api.repository.group.param.SaveApiGroupParam;
import com.apiflow.api.repository.group.param.SelectApiGroupParam;
import com.apiflow.api.repository.group.param.SelectOneApiGroupParam;
import com.apiflow.api.repository.group.param.UpdateApiGroupParam;
import com.apiflow.infrastructure.persistence.mybatis.group.converter.ApiGroupConverter;
import com.apiflow.infrastructure.persistence.mybatis.group.entity.ApiGroupPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ApiGroupRepositoryImpl implements ApiGroupRepository {

    private final ApiGroupMapper apiGroupMapper;

    @Override
    public ApiGroupIDTO save(SaveApiGroupParam param) {
        ApiGroupPO po = ApiGroupConverter.INSTANCE.saveParamToPO(param);
        apiGroupMapper.insert(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO update(UpdateApiGroupParam param) {
        ApiGroupPO po = ApiGroupConverter.INSTANCE.updateParamToPO(param);
        apiGroupMapper.updateById(po);
        return ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public ApiGroupIDTO selectOne(SelectOneApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupName, param.getGroupName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        ApiGroupPO po = apiGroupMapper.selectOne(wrapper);
        return po == null ? null : ApiGroupConverter.INSTANCE.poToIDTO(po);
    }

    @Override
    public List<ApiGroupIDTO> selectList(SelectApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupName, param.getGroupName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        wrapper.orderByDesc(ApiGroupPO::getCreateTimeMs);
        if (param.getLimit() != null) {
            wrapper.last("LIMIT " + param.getLimit());
        }
        List<ApiGroupPO> list = apiGroupMapper.selectList(wrapper);
        return list.stream()
                .map(ApiGroupConverter.INSTANCE::poToIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long count(SelectApiGroupParam param) {
        LambdaQueryWrapper<ApiGroupPO> wrapper = new LambdaQueryWrapper<>();
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupNo, param.getGroupNo());
        QueryConditionHelper.applyFieldCondition(wrapper, ApiGroupPO::getGroupName, param.getGroupName());
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        return apiGroupMapper.selectCount(wrapper);
    }
}
