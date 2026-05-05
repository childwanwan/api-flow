package com.flow.api.repository.user;

import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.api.repository.user.param.InsertUserParam;
import com.flow.api.repository.user.param.SelectOneUserParam;
import com.flow.api.repository.user.param.SelectUserParam;
import com.flow.api.repository.user.param.UpdateUserParam;

import java.util.List;

public interface UserRepository {

    UserIDTO selectOne(SelectOneUserParam param);

    List<UserIDTO> selectList(SelectUserParam param);

    int insert(InsertUserParam param);

    int update(UpdateUserParam param);

    int delete(Long id);

    long count(SelectUserParam param);

}