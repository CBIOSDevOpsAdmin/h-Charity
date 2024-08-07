package com.himanism.hcharityapi.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.himanism.hcharityapi.dto.response.EntityBankDetailsResDto;
import com.himanism.hcharityapi.entities.EntityBankDetails;

@Mapper
public interface EntityBankDetailsMapper {
    EntityBankDetailsMapper INSTANCE = Mappers.getMapper(EntityBankDetailsMapper.class);

    EntityBankDetailsResDto entityBankDetailsToEntityBankDetailsResDTO(EntityBankDetails entityBankDetails);
    EntityBankDetails entityBankDetailsResDTOToEntityBankDetails(EntityBankDetailsResDto bankDetailsResDto);

}
