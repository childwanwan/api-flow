package com.flow.infrastructure.persistence.mybatis.user;

import com.flow.api.repository.user.UserRepository;
import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.api.repository.user.param.InsertUserParam;
import com.flow.api.repository.user.param.SelectOneUserParam;
import com.flow.api.repository.user.param.SelectUserParam;
import com.flow.api.repository.user.param.UpdateUserParam;
import com.flow.infrastructure.persistence.mybatis.user.converter.UserConverter;
import com.flow.infrastructure.persistence.mybatis.user.entity.UserPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;

    @Override
    public UserIDTO selectOne(SelectOneUserParam param) {
        if (ObjectUtils.isEmpty(param) || param.isEmpty()) {
            return null;
        }
        UserPO userPO = userMapper.selectOne(buildQueryWrapper(param));
        return UserConverter.INSTANCE.userPO2UserIDTO(userPO);
    }

    @Override
    public List<UserIDTO> selectList(SelectUserParam param) {
        LambdaQueryWrapper<UserPO> wrapper = buildQueryWrapper(param);
        wrapper.orderByDesc(UserPO::getCreateTimeMs);
        List<UserPO> list = userMapper.selectList(wrapper);
        return list.stream()
                .map(UserConverter.INSTANCE::userPO2UserIDTO)
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
                .status(StringUtils.defaultIfBlank(param.getStatus(), "ENABLED"))
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
        return userMapper.selectCount(buildQueryWrapper(param));
    }

    private LambdaQueryWrapper<UserPO> buildQueryWrapper(SelectOneUserParam param) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        if (param != null) {
            if (StringUtils.isNotBlank(param.getUsername())) {
                wrapper.eq(UserPO::getUsername, param.getUsername());
            }
        }
        return wrapper;
    }

    private LambdaQueryWrapper<UserPO> buildQueryWrapper(SelectUserParam param) {
        LambdaQueryWrapper<UserPO> wrapper = new LambdaQueryWrapper<>();
        if (param != null) {
            if (StringUtils.isNotBlank(param.getUsername())) {
                wrapper.like(UserPO::getUsername, param.getUsername());
            }
            if (StringUtils.isNotBlank(param.getRole())) {
                wrapper.eq(UserPO::getRole, param.getRole());
            }
            if (StringUtils.isNotBlank(param.getStatus())) {
                wrapper.eq(UserPO::getStatus, param.getStatus());
            }
        }
        return wrapper;
    }
}