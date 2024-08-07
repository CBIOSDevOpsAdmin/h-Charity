package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.request.AppealRequestDto;
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.entities.Appeal;

@Mapper
public interface AppealMapper {

    AppealMapper INSTANCE = Mappers.getMapper(AppealMapper.class);

    AppealResDto appealToAppealResponseDTO(Appeal appeal);
    Appeal appealResponseDTOToAppeal(AppealResDto appealResDto);
    Appeal appealRequestDTOtoAppeal(AppealRequestDto appealRequestDto);
}
