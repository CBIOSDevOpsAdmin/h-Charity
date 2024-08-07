package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.response.EntityResponseDto;
import com.himanism.hcharityapi.entities.Entities;

@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    EntityResponseDto entityToEntityResponseDTO(Entities entity);
    Entities entityResponseDTOToEntity(EntityResponseDto entityResponseDto);
}
