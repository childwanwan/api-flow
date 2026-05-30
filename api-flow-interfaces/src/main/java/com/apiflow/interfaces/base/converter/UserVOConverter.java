package com.apiflow.interfaces.base.converter;

import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.interfaces.base.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserVOConverter {
    UserVOConverter INSTANCE = Mappers.getMapper(UserVOConverter.class);
    UserVO toVO(UserIDTO idto);
    List<UserVO> toVOList(List<UserIDTO> idtos);
}
