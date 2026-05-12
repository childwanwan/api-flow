package com.apiflow.application.user;

import com.apiflow.api.cache.CacheGateway;
import com.apiflow.common.enums.EnableStatus;
import com.apiflow.common.repository.FieldCondition;
import com.apiflow.common.util.MD5Util;
import com.apiflow.application.user.dto.UserDTO;
import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.api.repository.user.UserRepository;
import com.apiflow.api.repository.user.param.SelectOneUserParam;
import com.apiflow.api.repository.user.param.UpdateUserParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_PREFIX = "auth:token:";
    private static final long TOKEN_EXPIRE_HOURS = 24;

    private final UserRepository userRepository;
    private final CacheGateway cacheGateway;

    public String login(String username, String password) {
        SelectOneUserParam selectOneUserParam = SelectOneUserParam.builder()
                .username(FieldCondition.of(username))
                .build();
        UserIDTO user = userRepository.selectOne(selectOneUserParam);
        if (user == null) {
            return null;
        }
        if (!EnableStatus.ENABLED.getValue().equals(user.getStatus())) {
            return null;
        }
        // user的password已经是加密了的，参数password也是加密了的
        if (!user.getPassword().equals(password)) {
            return null;
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        cacheGateway.set(TOKEN_PREFIX + token, username, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        UpdateUserParam updateUserParam = UpdateUserParam.builder()
                .id(user.getId())
                .lastLoginTimeMs(System.currentTimeMillis())
                .build();
        userRepository.update(updateUserParam);
        return token;
    }

    public void logout(String token) {
        if (token != null) {
            cacheGateway.delete(TOKEN_PREFIX + token);
        }
    }

    public UserDTO getUserByToken(String token) {
        if (token == null) {
            return null;
        }
        String username = (String) cacheGateway.get(TOKEN_PREFIX + token);
        if (username == null) {
            return null;
        }
        SelectOneUserParam selectOneUserParam = SelectOneUserParam.builder()
                .username(FieldCondition.of(username))
                .build();
        UserIDTO user = userRepository.selectOne(selectOneUserParam);
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .status(user.getStatus())
                .createTimeMs(user.getCreateTimeMs())
                .updateTimeMs(user.getUpdateTimeMs())
                .lastLoginTimeMs(user.getLastLoginTimeMs())
                .build();
    }

    public String getUsernameByToken(String token) {
        if (token == null) {
            return null;
        }
        return (String) cacheGateway.get(TOKEN_PREFIX + token);
    }

    public boolean isValidToken(String token) {
        if (token == null) {
            return false;
        }
        return cacheGateway.hasKey(TOKEN_PREFIX + token);
    }

    public void refreshToken(String token) {
        if (isValidToken(token)) {
            cacheGateway.expire(TOKEN_PREFIX + token, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
        }
    }
}
