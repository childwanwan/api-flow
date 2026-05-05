package com.flow.infrastructure.persistence.mybatis.user.converter;

import com.flow.api.repository.user.dto.UserIDTO;
import com.flow.infrastructure.persistence.mybatis.user.entity.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "infrastructureUserConverter")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);


    UserIDTO userPO2UserIDTO(UserPO userPO);
}
