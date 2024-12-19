package com.himanism.hcharityapi.mappers;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", source = "roles", qualifiedByName = "firstRoleName")
    UserResDto userToUserResponseDTO(User user);

    User userRequestDTOToUser(UserReqDto userRequestDto);

    // Helper method to extract the name of the first Role in the set
    @Named("firstRoleName")
    default String mapFirstRole(Set<Role> roles) {
        return roles != null && !roles.isEmpty() ? roles.iterator().next().getName().name() : null;
    }
}
