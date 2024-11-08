package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.request.EntityRequestDto;
import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.entities.Entities;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    @Mapping(source = "user", target = "entityOwner")
    // @Mapping(source = "address", target = "address")
    @Mapping(target = "entityOwner.email", ignore = true)
    @Mapping(target = "entityOwner.mobile", ignore = true)
    @Mapping(target = "entityOwner.password", ignore = true)
    EntityResponseDto entityToEntityResponseDTO(Entities entity);

    Entities entityResponseDTOToEntity(EntityResponseDto entityResponseDto);

    Entities entityRequestDTOToEntity(EntityRequestDto entityRequestDto);
}
