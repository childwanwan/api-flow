package com.apiflow.infrastructure.persistence.mybatis.user.converter;

import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.infrastructure.persistence.mybatis.user.entity.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(implementationName = "infrastructureUserConverter")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);


    UserIDTO userPOToUserIDTO(UserPO userPO);
}
