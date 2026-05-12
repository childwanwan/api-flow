package com.apiflow.infrastructure.persistence.mybatis.user;

import com.apiflow.common.enums.EnableStatus;
import com.apiflow.api.repository.user.UserRepository;
import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.api.repository.user.param.InsertUserParam;
import com.apiflow.api.repository.user.param.SelectOneUserParam;
import com.apiflow.api.repository.user.param.SelectUserParam;
import com.apiflow.api.repository.user.param.UpdateUserParam;
import com.apiflow.api.repository.user.param.UserField;
import com.apiflow.infrastructure.persistence.mybatis.user.converter.UserConverter;
import com.apiflow.infrastructure.persistence.mybatis.user.entity.UserPO;
import com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.apiflow.infrastructure.persistence.mybatis.util.QueryConditionHelper.createFieldResolver;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;

    @Override
    public UserIDTO selectOne(SelectOneUserParam param) {
        if (ObjectUtils.isEmpty(param) || param.isEmpty()) {
            return null;
        }
        if (param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            if (param.getUsername() != null) {
                QueryConditionHelper.applyFieldCondition(wrapper, "username", param.getUsername());
            }
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(UserField.values()));
            UserPO userPO = userMapper.selectOne(wrapper);
            return UserConverter.INSTANCE.userPOToUserIDTO(userPO);
        }
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        if (param.getUsername() != null) {
            QueryConditionHelper.applyFieldCondition(wrapper, UserPO::getUsername, param.getUsername());
        }
        QueryConditionHelper.applyConditions(wrapper, param.getConditions());
        QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(UserField.values()));
        UserPO userPO = userMapper.selectOne(wrapper);
        return UserConverter.INSTANCE.userPOToUserIDTO(userPO);
    }

    @Override
    public List<UserIDTO> selectList(SelectUserParam param) {
        if (param != null && param.getSelectFields() != null && !param.getSelectFields().isEmpty()) {
            QueryWrapper<UserPO> wrapper = new QueryWrapper<>();
            QueryConditionHelper.applySelectFields(wrapper, param.getSelectFields());
            applyConditions(wrapper, param);
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(UserField.values()));
            wrapper.orderByDesc("create_time_ms");
            List<UserPO> list = userMapper.selectList(wrapper);
            return list.stream()
                    .map(UserConverter.INSTANCE::userPOToUserIDTO)
                    .collect(Collectors.toList());
        }
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        applyLambdaConditions(wrapper, param);
        if (param != null) {
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(UserField.values()));
        }
        wrapper.orderByDesc(UserPO::getCreateTimeMs);
        List<UserPO> list = userMapper.selectList(wrapper);
        return list.stream()
                .map(UserConverter.INSTANCE::userPOToUserIDTO)
                .collect(Collectors.toList());
    }

    @Override
    public int insert(InsertUserParam param) {
        if (ObjectUtils.isEmpty(param)) {
            return 0;
        }
        UserPO userPO = UserPO.builder()
                .username(param.getUsername())
                .password(param.getPassword())
                .role(StringUtils.defaultIfBlank(param.getRole(), "USER"))
                .status(StringUtils.defaultIfBlank(param.getStatus(), EnableStatus.ENABLED.getValue()))
                .createOperator(param.getCreateOperator())
                .createTimeMs(System.currentTimeMillis())
                .updateTimeMs(System.currentTimeMillis())
                .build();
        return userMapper.insert(userPO);
    }

    @Override
    public int update(UpdateUserParam param) {
        if (ObjectUtils.isEmpty(param) || ObjectUtils.isEmpty(param.getId())) {
            return 0;
        }
        UserPO userPO = new UserPO();
        userPO.setId(param.getId());
        if (StringUtils.isNotBlank(param.getPassword())) {
            userPO.setPassword(param.getPassword());
        }
        if (StringUtils.isNotBlank(param.getRole())) {
            userPO.setRole(param.getRole());
        }
        if (StringUtils.isNotBlank(param.getStatus())) {
            userPO.setStatus(param.getStatus());
        }
        if (param.getLastLoginTimeMs() != null) {
            userPO.setLastLoginTimeMs(param.getLastLoginTimeMs());
        }
        userPO.setUpdateTimeMs(System.currentTimeMillis());
        return userMapper.updateById(userPO);
    }

    @Override
    public int delete(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return 0;
        }
        return userMapper.deleteById(id);
    }

    @Override
    public long count(SelectUserParam param) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        applyLambdaConditions(wrapper, param);
        if (param != null) {
            QueryConditionHelper.applyConditions(wrapper, param.getConditions());
            QueryConditionHelper.applyConditionNode(wrapper, param.getCondition(), createFieldResolver(UserField.values()));
        }
        return userMapper.selectCount(wrapper);
    }

    private void applyConditions(QueryWrapper<UserPO> wrapper, SelectUserParam param) {
        if (param != null) {
            QueryConditionHelper.applyFieldCondition(wrapper, "username", param.getUsername());
            QueryConditionHelper.applyFieldCondition(wrapper, "role", param.getRole());
            QueryConditionHelper.applyFieldCondition(wrapper, "status", param.getStatus());
        }
    }

    private void applyLambdaConditions(LambdaQueryWrapper<UserPO> wrapper, SelectUserParam param) {
        if (param != null) {
            QueryConditionHelper.applyFieldCondition(wrapper, UserPO::getUsername, param.getUsername());
            QueryConditionHelper.applyFieldCondition(wrapper, UserPO::getRole, param.getRole());
            QueryConditionHelper.applyFieldCondition(wrapper, UserPO::getStatus, param.getStatus());
        }
    }
}
