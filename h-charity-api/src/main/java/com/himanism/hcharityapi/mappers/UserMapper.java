package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    UserResDto userToUserResponseDTO(User user);

    // Appeal appealResponseDTOToAppeal(AppealResDto appealResDto);

    // Appeal appealRequestDTOtoAppeal(AppealRequestDto appealRequestDto);
}
