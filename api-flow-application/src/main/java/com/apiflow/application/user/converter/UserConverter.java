package com.apiflow.application.user.converter;

import com.apiflow.api.repository.user.idto.UserIDTO;
import com.apiflow.application.user.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
    UserDTO toDTO(UserIDTO idto);
}
