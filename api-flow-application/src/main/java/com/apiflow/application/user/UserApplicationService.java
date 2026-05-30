package com.apiflow.application.user;

import com.apiflow.common.repository.FieldCondition;
import com.apiflow.api.repository.user.UserRepository;
import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.api.repository.user.param.InsertUserParam;
import com.apiflow.api.repository.user.param.SelectOneUserParam;
import com.apiflow.api.repository.user.param.SelectUserParam;
import com.apiflow.api.repository.user.param.UpdateUserParam;
import com.apiflow.api.repository.user.param.UserField;
import com.apiflow.common.repository.QueryCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;

    public List<UserIDTO> getUserList(SelectUserParam param) {
        return userRepository.selectList(param);
    }

    public long count(SelectUserParam param) {
        return userRepository.count(param);
    }

    public UserIDTO getUserById(Long id) {
        SelectOneUserParam param = SelectOneUserParam.builder()
                .conditions(List.of(QueryCondition.eq(UserField.ID, id)))
                .build();
        return userRepository.selectOne(param);
    }

    public boolean existsByUsername(String username) {
        SelectUserParam param = SelectUserParam.builder().username(FieldCondition.of(username)).build();
        return userRepository.count(param) > 0;
    }

    public boolean createUser(InsertUserParam param) {
        return userRepository.insert(param) > 0;
    }

    public boolean updateUser(UpdateUserParam param) {
        return userRepository.update(param) > 0;
    }

    public boolean deleteUser(Long id) {
        return userRepository.delete(id) > 0;
    }
}
